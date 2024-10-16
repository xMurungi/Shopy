package com.ag_apps.core.domain

import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.EmptyResult
import com.ag_apps.core.domain.util.Error
import com.ag_apps.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * @author Ahmed Guedmioui
 */
interface ProductDataSource {

    suspend fun getProducts(refresh: Boolean): Result<List<Product>, DataError.Network>

    suspend fun getProduct(id: String): Result<Product, DataError.Network>

    suspend fun getCategories(): Result<List<Category>, DataError.Network>

    suspend fun getCategory(id: String): Result<Category, DataError.Network>

    suspend fun getCategoryProducts(id: String): Result<List<Product>, DataError.Network>


}