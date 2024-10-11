package com.ag_apps.auth.data

import com.ag_apps.auth.domain.AuthRepository
import com.ag_apps.core.domain.User
import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.domain.EmptyFieldsUser
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.EmptyResult
import com.ag_apps.core.domain.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

/**
 * @author Ahmed Guedmioui
 */
class AuthRepositoryImpl(
    private val googleCredentialClient: GoogleCredentialClient,
    private val userDataSource: UserDataSource,
    private val applicationScope: CoroutineScope,
) : AuthRepository {

    private val tag = "AuthRepository"

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun register(
        email: String, name: String, password: String
    ): Flow<EmptyResult<DataError.Network>> {
        return callbackFlow {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Timber.tag(tag).d("user registered successfully")

                        applicationScope.launch {
                            val user = EmptyFieldsUser.copy(
                                name = name, email = email, id = ""
                            )
                            insertUser(user).collect {
                                trySend(it)
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        Timber.tag(tag).d("user email already used ${e.message}")
                        trySend(Result.Error(DataError.Network.CONFLICT))
                    }

            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                Timber.tag(tag).d("could not register user ${e.message}")
                trySend(Result.Error(DataError.Network.UNKNOWN))
            }

            awaitClose { close() }
        }
    }

    override suspend fun login(
        email: String, password: String
    ): Flow<EmptyResult<DataError.Network>> {
        return callbackFlow {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Timber.tag(tag).d("user logged in successfully")
                        trySend(Result.Success(Unit))
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        Timber.tag(tag).d("incorrect password or email ${e.message}")
                        trySend(Result.Error(DataError.Network.UNAUTHORIZED))
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                Timber.tag(tag).d("could not login user ${e.message}")
                trySend(Result.Error(DataError.Network.UNKNOWN))
            }

            awaitClose { close() }
        }
    }

    override suspend fun googleSignIn(): Flow<EmptyResult<DataError.Network>> {
        return callbackFlow {
            val tokenCredentialResult = googleCredentialClient.getTokenCredential()
            if (tokenCredentialResult is Result.Success) {

                val credential = tokenCredentialResult.data
                val authCredential = GoogleAuthProvider.getCredential(
                    credential.idToken, null
                )

                val authResult = firebaseAuth.signInWithCredential(authCredential).await()

                if (authResult.user != null) {
                    Timber.tag(tag).d("user google signed in successfully")

                    // - when user does not already exit in the data store (Result.Error),
                    //   then they are registering with google sign in and we need to insert them.
                    // - use pass null for email since user logged in and already
                    //   exits in firebaseAuth by now
                    userDataSource.getUser(null).collect { userResult ->
                        when (userResult) {
                            is Result.Error -> {
                                val user = EmptyFieldsUser.copy(
                                    name = credential.displayName ?: "",
                                    email = credential.id,
                                    id = ""
                                )
                                insertUser(user).collect { trySend(it) }
                            }

                            is Result.Success -> {
                                trySend(Result.Success(Unit))
                            }
                        }
                    }
                } else {
                    Timber.tag(tag).d("Could not google sign in user")
                    trySend(Result.Error(DataError.Network.UNAUTHORIZED))
                }

            } else {
                Timber.tag(tag)
                    .d("could get user credential ${(tokenCredentialResult as Result.Error).error}")
                trySend(tokenCredentialResult as Result.Error)
            }

            awaitClose { close() }
        }
    }

    private fun insertUser(user: User): Flow<EmptyResult<DataError.Network>> {
        return callbackFlow {
            userDataSource.insertUser(user).collect { insertUserResult ->

                if (insertUserResult is Result.Success) {
                    Timber.tag(tag).d("user inserted to firestore successfully")
                    trySend(Result.Success(Unit))
                } else {
                    Timber.tag(tag)
                        .d("could not insert user ${(insertUserResult as Result.Error).error}")
                    firebaseAuth.currentUser?.delete()
                    trySend(insertUserResult as Result.Error)
                }
            }

            awaitClose { close() }
        }
    }

}
















