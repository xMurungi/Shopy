package com.ag_apps.search.domain

import com.ag_apps.core.domain.Product
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
interface SearchRepository {

    suspend fun searchProducts(
        query: String,
        offset: Int,
        minPrice: Int? = null,
        maxPrice: Int? = null,
    ): Result<List<Product>, DataError.Network>

    suspend fun getProduct(
        productId: Int
    ): Result<Product, DataError.Network>

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