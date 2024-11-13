package com.ag_apps.core.domain.abstractions

import com.ag_apps.core.domain.models.Category
import com.ag_apps.core.domain.models.Product
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
interface ProductDataSource {

    // only needed because Render where the server is deployed,
    // delays the very first request after not being active for a while.
    // if I deployed the server somewhere else it would not be needed.
    suspend fun wakeupPaymentServer()

    suspend fun getProducts(
        query: String? = null,
        offset: Int,
        minPrice: Int? = null,
        maxPrice: Int? = null,
    ): Result<List<Product>, DataError.Network>


    suspend fun getProduct(productId: Int): Result<Product, DataError.Network>

    suspend fun getCategories(): Result<List<Category>, DataError.Network>

    suspend fun getCategory(categoryId: Int): Result<Category, DataError.Network>

    suspend fun getCategoryProducts(categoryId: Int): Result<List<Product>, DataError.Network>


}