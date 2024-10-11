package com.ag_apps.core.network

import com.ag_apps.core.domain.User
import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author Ahmed Guedmioui
 */
class FirebaseUserDataSource(
    private val firestoreClient: FirestoreClient
) : UserDataSource {

    private val tag = "UserDataSource"

    private var firebaseAuth = FirebaseAuth.getInstance()

    override suspend fun insertUser(user: User): Flow<Result<String, DataError.Network>> {
        return flow {
            firestoreClient.insertUser(user).collect {
                Timber.tag(tag).d("insertUser: ${it is Result.Success}")
                emit(it)
            }
        }
    }

    override suspend fun updateUser(user: User): Flow<Result<String, DataError.Network>> {
        return flow {
            firestoreClient.updateUser(user).collect {
                Timber.tag(tag).d("updateUser: ${it is Result.Success}")
                emit(it)
            }
        }
    }

    override suspend fun getUser(email: String?): Flow<Result<User, DataError.Network>> {
        return flow {

            val userEmail = email ?: firebaseAuth.currentUser?.email
            if (userEmail == null) {
                emit(Result.Error(DataError.Network.NOT_FOUND))
                return@flow
            }

            firestoreClient.getUser(userEmail).collect {
                Timber.tag(tag).d("getUser: ${it is Result.Success}")
                emit(it)
            }
        }
    }

    override fun isLoggedIn(): Boolean {
        if (firebaseAuth.currentUser != null) {
            Timber.tag(tag).d("Already logged in")
            return true
        }

        Timber.tag(tag).d("Not already logged in")
        return false
    }

    override fun logout() {
        Timber.tag(tag).d("logout")
        firebaseAuth.signOut()
    }
}
