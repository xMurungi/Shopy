package com.ag_apps.auth.data

import com.ag_apps.auth.domain.AuthRepository
import com.ag_apps.core.domain.SessionStorage
import com.ag_apps.core.domain.User
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.EmptyResult
import com.ag_apps.core.domain.util.Result
import com.ag_apps.core.domain.util.asEmptyDataResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author Ahmed Guedmioui
 */
class AuthRepositoryImpl(
    private val googleAuthClient: GoogleAuthClient,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope,
) : AuthRepository {

    private val TAG = "AuthRepositoryImpl"

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun register(
        email: String, name: String, password: String
    ): EmptyResult<DataError.Network> {
        try {
            val authResult = suspendCoroutine { continuation ->
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            setUser(
                                User(name = name, id = email, profilePicture = "")
                            )
                            continuation.resume(Result.Success(Unit))
                        } else {
                            task.exception?.printStackTrace()
                            continuation.resume(Result.Error(DataError.Network.UNAUTHORIZED))
                        }
                    }
            }
            return authResult
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            return Result.Error(DataError.Network.UNAUTHORIZED)
        }
    }

    override suspend fun login(
        email: String, password: String
    ): EmptyResult<DataError.Network> {
        try {
            val authResult = suspendCoroutine { continuation ->
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        setUser(
                            User(
                                name = task.result.additionalUserInfo?.username ?: "",
                                id = email,
                                profilePicture = task.result.user?.photoUrl.toString(),
                            )
                        )
                        task.exception?.printStackTrace()
                        continuation.resume(Result.Success(Unit))
                    } else {
                        task.exception?.printStackTrace()
                        continuation.resume(Result.Error(DataError.Network.UNAUTHORIZED))
                    }
                }
            }
            return authResult
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            return Result.Error(DataError.Network.UNAUTHORIZED)
        }
    }

    private fun setUser(user: User) {
        applicationScope.launch {
            sessionStorage.set(user)
        }
    }

    override suspend fun googleLogin(): EmptyResult<DataError.Network> {
        val result = googleAuthClient.logIn()
        if (result is Result.Success) {
            sessionStorage.set(
                User(
                    name = result.data.name,
                    id = result.data.id,
                    profilePicture = result.data.profilePicture
                )
            )
        }

        return result.asEmptyDataResult()
    }
}
















