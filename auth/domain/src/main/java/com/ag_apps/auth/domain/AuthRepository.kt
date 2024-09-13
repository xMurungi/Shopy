package com.ag_apps.auth.domain

import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow


/**
 * @author Ahmed Guedmioui
 */
interface AuthRepository {
    suspend fun register(
        email: String, name: String, password: String
    ): EmptyResult<DataError.Network>

    suspend fun login(
        email: String, password: String
    ): EmptyResult<DataError.Network>

    suspend fun googleLogin(): EmptyResult<DataError.Network>
}

















