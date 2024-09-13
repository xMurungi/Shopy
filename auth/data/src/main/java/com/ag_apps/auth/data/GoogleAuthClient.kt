package com.ag_apps.auth.data

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.core.domain.User
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CancellationException
import timber.log.Timber


/**
 * @author Ahmed Guedmioui
 */
class GoogleAuthClient(
    private val context: Context,
) {

    private val TAG = "GoogleAuthUiClient"

    private suspend fun buildCredentialRequest(): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.FIREBASE_WEB_CLIENT_ID)
                    .setAutoSelectEnabled(false)
                    .build()
            )
            .build()

        return CredentialManager.create(context).getCredential(
            request = request, context = context
        )
    }

    suspend fun logIn(): Result<User, DataError.Network> {
        try {
            val result = buildCredentialRequest()
            return handleSignIn(result)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            return Result.Error(DataError.Network.UNKNOWN)
        }
    }

    private fun handleSignIn(result: GetCredentialResponse): Result<User, DataError.Network> {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                        Timber.tag(TAG).d("login: ${tokenCredential.displayName}")
                        Timber.tag(TAG).d("login: ${tokenCredential.id}")
                        Timber.tag(TAG).d("login: ${tokenCredential.profilePictureUri}")

                        return Result.Success(
                            User(
                                name = tokenCredential.displayName ?: "",
                                id = tokenCredential.id,
                                profilePicture = tokenCredential.profilePictureUri.toString()
                            )
                        )

                    } catch (e: GoogleIdTokenParsingException) {
                        Timber.tag(TAG).d("Received an invalid google id token response")
                        return Result.Error(DataError.Network.UNAUTHORIZED)
                    }
                } else {
                    Timber.tag(TAG).d("Unexpected type of credential")
                    return Result.Error(DataError.Network.UNAUTHORIZED)
                }
            }

            else -> {
                Timber.tag(TAG).d("Unexpected type of credential")
                return Result.Error(DataError.Network.UNAUTHORIZED)
            }
        }
    }

}