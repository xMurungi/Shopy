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
    ): Flow<EmptyResult<DataError.Network>>

    suspend fun login(
        email: String, password: String
    ): Flow<EmptyResult<DataError.Network>>

    suspend fun googleSignIn(): Flow<EmptyResult<DataError.Network>>
}

















