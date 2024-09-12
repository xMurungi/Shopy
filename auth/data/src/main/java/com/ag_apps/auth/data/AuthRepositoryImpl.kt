package com.ag_apps.auth.data

import com.ag_apps.auth.domain.AuthRepository
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.EmptyResult
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
class AuthRepositoryImpl(
) : AuthRepository {
    override suspend fun register(
        email: String, password: String
    ): EmptyResult<DataError.Network> {

        return Result.Success(Unit)
    }

    override suspend fun login(
        email: String, password: String
    ): EmptyResult<DataError.Network> {

        return Result.Success(Unit)
    }
}
















