package com.ag_apps.core.domain

import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
interface ProductDataSource {

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