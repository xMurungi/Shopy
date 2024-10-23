package com.ag_apps.product.domain

import com.ag_apps.core.domain.Category
import com.ag_apps.core.domain.Product
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
interface ProductRepository {

    suspend fun getProducts(
        offset: Int,
        minPrice: Int? = null,
        maxPrice: Int? = null,
    ): Result<List<Product>, DataError.Network>

    suspend fun addProductToWishlist(
        productId: String
    ): Result<Unit, DataError.Network>

    suspend fun removeProductFromWishlist(
        productId: String
    ): Result<Unit, DataError.Network>

    suspend fun addProductToCart(
        productId: String
    ): Result<Unit, DataError.Network>

    suspend fun removeProductFromCart(
        productId: String
    ): Result<Unit, DataError.Network>

    suspend fun getCategories(): Result<List<Category>, DataError.Network>


}