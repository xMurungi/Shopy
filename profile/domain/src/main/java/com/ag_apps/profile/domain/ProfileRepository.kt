package com.ag_apps.profile.domain

import com.ag_apps.core.domain.User
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * @author Ahmed Guedmioui
 */
interface ProfileRepository {

    suspend fun updateUser(user: User?): Result<String, DataError.Network>

    suspend fun getUser(): Result<User, DataError.Network>

    fun logout()

}