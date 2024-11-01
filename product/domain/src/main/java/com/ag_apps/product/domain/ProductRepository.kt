package com.ag_apps.product.domain

import com.ag_apps.core.domain.models.Category
import com.ag_apps.core.domain.models.Product
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
        productId: Int, filter: String?
    ): Result<Unit, DataError.Network>

    suspend fun removeProductFromCart(
        productId: Int
    ): Result<Unit, DataError.Network>

    suspend fun getCategories(): Result<List<Category>, DataError.Network>


}