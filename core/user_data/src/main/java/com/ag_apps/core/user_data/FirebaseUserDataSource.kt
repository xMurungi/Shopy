package com.ag_apps.core.user_data

import com.ag_apps.core.domain.User
import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    override suspend fun updateUser(user: User?): Flow<Result<String, DataError.Network>> {
        return flow {
            if (user == null) {
                emit(Result.Error(DataError.Network.UNKNOWN))
                return@flow
            }
            firestoreClient.updateUser(user).collect {
                Timber.tag(tag).d("updateUser: ${it is Result.Success}")
                emit(it)
            }
        }
    }

    override suspend fun getUser(): Flow<Result<User, DataError.Network>> {
        return flow {

            val userEmail = firebaseAuth.currentUser?.email
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
