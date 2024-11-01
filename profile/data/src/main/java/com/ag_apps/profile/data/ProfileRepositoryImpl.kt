package com.ag_apps.profile.data

import com.ag_apps.core.domain.models.User
import com.ag_apps.core.domain.abstractions.UserDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.profile.domain.ProfileRepository

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