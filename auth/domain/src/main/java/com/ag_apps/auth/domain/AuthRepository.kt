package com.ag_apps.auth.domain

import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.EmptyResult


/**
 * @author Ahmed Guedmioui
 */
interface AuthRepository {
    suspend fun register(
        email: String, password: String
    ): EmptyResult<DataError.Network>

    suspend fun login(
        email: String, password: String
    ): EmptyResult<DataError.Network>
}

















