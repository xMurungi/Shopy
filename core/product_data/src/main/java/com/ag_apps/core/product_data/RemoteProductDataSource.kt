package com.ag_apps.core.product_data

import com.ag_apps.core.domain.Category
import com.ag_apps.core.domain.Product
import com.ag_apps.core.domain.ProductDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.core.domain.util.map
import com.ag_apps.core.product_data.dto.CategoriesDto
import com.ag_apps.core.product_data.dto.CategoryDto
import com.ag_apps.core.product_data.dto.ProductDto
import com.ag_apps.core.product_data.dto.ProductsDto
import com.ag_apps.core.product_data.dto.toCategory
import com.ag_apps.core.product_data.dto.toProduct
import io.ktor.client.HttpClient

/**
 * @author Ahmed Guedmioui
 */
class RemoteProductDataSource(
    private val httpClient: HttpClient
) : ProductDataSource {

    private var currentOffset = 0

    override suspend fun getProducts(
        refresh: Boolean, minPrice: Int?, maxPrice: Int?,
    ): Result<List<Product>, DataError.Network> {

        if (refresh) {
            currentOffset = 0
        } else {
            currentOffset += 10
        }

        val queryParameters = if (minPrice != null && maxPrice != null) {
            mapOf(
                "price_min" to minPrice,
                "price_max" to maxPrice,
                "offset" to currentOffset,
                "limit" to 10,
        } else {
            mapOf(
                "offset" to currentOffset,
                "limit" to 10,
            )
        }

        return httpClient.get<ProductsDto>(
            route = "/products",
            queryParameters = queryParameters
        ).map { productsDto ->
            productsDto.map { it.toProduct() }
        }
    }

    override suspend fun searchProducts(
        query: String,
        minPrice: Int?,
        maxPrice: Int?
    ): Result<List<Product>, DataError.Network> {
        val queryParameters = if (minPrice != null && maxPrice != null) {
            mapOf(
                "title" to query,
                "price_min" to minPrice,
                "price_max" to maxPrice,
                "offset" to currentOffset,
                "limit" to 10,
        } else {
            mapOf(
                "title" to query,
                "offset" to currentOffset,
                "limit" to 10,
            )
        }

        return httpClient.get<ProductsDto>(
            route = "/products",
            queryParameters = queryParameters
        ).map { productsDto ->
            productsDto.map { it.toProduct() }
        }
    }

    override suspend fun getProduct(
        id: String
    ): Result<Product, DataError.Network> {
        return httpClient.get<ProductDto>(
            route = "/products/$id"
        ).map { productDto ->
            productDto.toProduct()
        }
    }

    override suspend fun getCategories(): Result<List<Category>, DataError.Network> {
        return httpClient.get<CategoriesDto>(
            route = "/categories"
        ).map { categoriesDto ->
            categoriesDto.map { it.toCategory() }
        }
    }

    override suspend fun getCategory(
        id: String
    ): Result<Category, DataError.Network> {
        return httpClient.get<CategoryDto>(
            route = "/categories/$id"
        ).map { categoryDto ->
            categoryDto.toCategory()
        }
    }

    override suspend fun getCategoryProducts(
        id: String
    ): Result<List<Product>, DataError.Network> {
        return httpClient.get<ProductsDto>(
            route = "/categories/$id/products"
        ).map { productsDto ->
            productsDto.map { it.toProduct() }
        }
    }


}