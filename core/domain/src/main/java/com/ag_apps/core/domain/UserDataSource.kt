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

    suspend fun insertUser(user: User): Result<String, DataError.Network>

    suspend fun updateUser(user: User): Result<String, DataError.Network>

    suspend fun getUser(): Result<User, DataError.Network>

    suspend fun addProductToWishlist(productId: Int): Result<Unit, DataError.Network>

    suspend fun removeProductToWishlist(productId: Int): Result<Unit, DataError.Network>

    suspend fun addProductToCart(productId: Int): Result<Unit, DataError.Network>

    suspend fun removeProductToCart(productId: Int): Result<Unit, DataError.Network>


    fun isLoggedIn(): Boolean

    fun logout()

}