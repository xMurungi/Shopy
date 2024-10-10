package com.ag_apps.auth.data

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CancellationException
import timber.log.Timber


/**
 * @author Ahmed Guedmioui
 */
class GoogleCredentialClient(
    private val context: Context,
) {

    private val tag = "GoogleCredentialClient"

    private val credentialManager = CredentialManager.create(context)

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

        return credentialManager.getCredential(
            request = request, context = context
        )
    }

    suspend fun getTokenCredential(): Result<GoogleIdTokenCredential, DataError.Network> {
        try {
            val result = buildCredentialRequest()
            return getCredential(result)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            Timber.tag(tag).d("getTokenCredentialException: ${e.message}")
            return Result.Error(DataError.Network.NO_GOOGLE_ACCOUNT)
        }
    }

    private fun getCredential(
        result: GetCredentialResponse
    ): Result<GoogleIdTokenCredential, DataError.Network> {

        val credential = result.credential

        if (
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {

            try {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                Timber.tag(tag).d("login: ${tokenCredential.displayName}")
                Timber.tag(tag).d("login: ${tokenCredential.id}")
                Timber.tag(tag).d("login: ${tokenCredential.profilePictureUri}")

                return Result.Success(tokenCredential)

            } catch (e: GoogleIdTokenParsingException) {
                Timber.tag(tag).d("GoogleIdTokenParsingException: ${e.message}")
                return Result.Error(DataError.Network.PARSING_ERROR)
            }
        } else {
            Timber.tag(tag).d("credential is not GoogleIdTokenCredential")
            return Result.Error(DataError.Network.UNEXPECTED_TYPE)
        }
    }

}