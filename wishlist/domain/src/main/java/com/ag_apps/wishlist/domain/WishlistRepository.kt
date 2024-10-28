package com.ag_apps.wishlist.domain

import com.ag_apps.core.domain.Product
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
interface WishlistRepository {

    suspend fun getWishlistProducts(): Result<List<Product>, DataError.Network>

    suspend fun addProductToWishlist(
        productId: Int
    ): Result<Unit, DataError.Network>

    suspend fun removeProductFromWishlist(
        productId: Int
    ): Result<Unit, DataError.Network>

    suspend fun addProductToCart(
        productId: Int
    ): Result<Unit, DataError.Network>

    suspend fun removeProductFromCart(
        productId: Int
    ): Result<Unit, DataError.Network>

}