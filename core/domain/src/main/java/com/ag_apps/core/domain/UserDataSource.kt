package com.ag_apps.core.domain

import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.EmptyResult
import com.ag_apps.core.domain.util.Error
import com.ag_apps.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * @author Ahmed Guedmioui
 */
interface UserDataSource {

    suspend fun insertUser(user: User): Flow<Result<String, DataError.Network>>

    suspend fun updateUser(user: User): Flow<Result<String, DataError.Network>>

    suspend fun getUser(email: String?): Flow<Result<User, DataError.Network>>

    fun isLoggedIn(): Boolean

    fun logout()

}