package com.ag_apps.profile.data

import com.ag_apps.core.domain.User
import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author Ahmed Guedmioui
 */
class ProfileRepositoryImpl(
    private val userDataSource: UserDataSource
) : ProfileRepository {

    override suspend fun updateUser(
        user: User?
    ): Result<String, DataError.Network> {
        if (user == null) {
            return Result.Error(DataError.Network.UNKNOWN)
        }
        return userDataSource.updateUser(user)
    }

    override suspend fun getUser(): Result<User, DataError.Network> {
        return userDataSource.getUser()
    }

    override fun logout() {
        return userDataSource.logout()
    }
}