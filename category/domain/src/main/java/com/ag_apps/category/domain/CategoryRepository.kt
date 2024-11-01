package com.ag_apps.category.domain

import com.ag_apps.core.domain.models.Category
import com.ag_apps.core.domain.models.Product
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
interface CategoryRepository {

    suspend fun getCategoryProducts(
        categoryId: Int
    ): Result<List<Product>, DataError.Network>

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

    suspend fun getCategory(categoryId: Int): Result<Category, DataError.Network>

}