package com.ag_apps.core.domain.abstractions

import com.ag_apps.core.domain.models.User
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
interface UserDataSource {

    suspend fun insertUser(user: User): Result<String, DataError.Network>

    suspend fun updateUser(user: User): Result<String, DataError.Network>

    suspend fun getUser(): Result<User, DataError.Network>

    suspend fun addProductToWishlist(productId: Int): Result<Unit, DataError.Network>

    suspend fun removeProductFromWishlist(productId: Int): Result<Unit, DataError.Network>

    suspend fun addProductToCart(
        productId: Int, filter: String?
    ): Result<Unit, DataError.Network>

    suspend fun removeProductFromCart(productId: Int): Result<Unit, DataError.Network>

    fun isLoggedIn(): Boolean

    fun logout()

}