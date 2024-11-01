package com.ag_apps.profile.domain

import com.ag_apps.core.domain.models.User
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
interface ProfileRepository {

    suspend fun updateUser(user: User?): Result<String, DataError.Network>

    suspend fun getUser(): Result<User, DataError.Network>

    fun logout()

}