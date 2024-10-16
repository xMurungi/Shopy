package com.ag_apps.product.domain

import com.ag_apps.core.domain.Product
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * @author Ahmed Guedmioui
 */
interface ProductRepository {

    suspend fun getProducts(
        refresh: Boolean, minPrice: Int?, maxPrice: Int?,
    ): Result<List<Product>, DataError.Network>

    suspend fun addProductToWishlist(
        productId: String
    ): Flow<Result<String, DataError.Network>>

    suspend fun addProductToCart(
        productId: String
    ): Flow<Result<String, DataError.Network>>

}