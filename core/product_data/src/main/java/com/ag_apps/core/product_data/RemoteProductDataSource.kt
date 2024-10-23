package com.ag_apps.core.product_data

import com.ag_apps.core.domain.Category
import com.ag_apps.core.domain.Product
import com.ag_apps.core.domain.ProductDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.core.domain.util.map
import com.ag_apps.core.product_data.dto.CategoryDto
import com.ag_apps.core.product_data.dto.ProductDto
import com.ag_apps.core.product_data.dto.toCategory
import com.ag_apps.core.product_data.dto.toProduct
import io.ktor.client.HttpClient
import timber.log.Timber

/**
 * @author Ahmed Guedmioui
 */
class RemoteProductDataSource(
    private val httpClient: HttpClient
) : ProductDataSource {

    private val tag = "ProductDataSource"

    override suspend fun getProducts(
        offset: Int,
        minPrice: Int?,
        maxPrice: Int?,
    ): Result<List<Product>, DataError.Network> {

        Timber.tag(tag).d(
            "getProducts: offset: $offset, minPrice: $minPrice, maxPrice: $maxPrice"
        )

        val queryParameters =
            if (minPrice != null && maxPrice != null) {
                mapOf(
                    "price_min" to minPrice,
                    "price_max" to maxPrice,
                    "offset" to offset,
                    "limit" to 10
                )
            } else {
                mapOf(
                    "offset" to offset,
                    "limit" to 10
                )
            }

        val productsResult = httpClient.get<List<ProductDto>>(
            route = "/products",
            queryParameters = queryParameters
        )

        when (productsResult) {
            is Result.Success -> {
                Timber.tag(tag).d("getProducts: Success ${productsResult.data.size}")

                val products = productsResult.data
                    .filter {
                        it.images.none { image ->
                            image.contains("[", ignoreCase = true) ||
                                    image.contains("]", ignoreCase = true) ||
                                    image.contains("\"", ignoreCase = true)
                        }
                    }
                    .map { it.toProduct() }

                return Result.Success(products)
            }

            is Result.Error -> {
                Timber.tag(tag).d("getProducts: Error ${productsResult.error}")
                return Result.Error(productsResult.error)
            }
        }
    }

    override suspend fun searchProducts(
        query: String,
        offset: Int,
        minPrice: Int?,
        maxPrice: Int?,
    ): Result<List<Product>, DataError.Network> {
        val queryParameters =
            if (minPrice != null && maxPrice != null) {
                mapOf(
                    "title" to query,
                    "price_min" to minPrice,
                    "price_max" to maxPrice,
                    "offset" to offset,
                    "limit" to 10
                )
            } else {
                mapOf(
                    "title" to query,
                    "offset" to offset,
                    "limit" to 10
                )
            }

        return httpClient.get<List<ProductDto>>(
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
        val categoriesResult = httpClient.get<List<CategoryDto>>(
            route = "/categories"
        )

        when (categoriesResult) {
            is Result.Success -> {
                val categories = categoriesResult.data
                    .shuffled()
                    .filter {category ->
                                // Exclude categories based on name
                                !category.name.contains("New", ignoreCase = true) &&
                                !category.name.contains("Category", ignoreCase = true) &&
                                !category.name.contains("Change", ignoreCase = true) &&
                                !category.name.contains("Title", ignoreCase = true) &&

                                // Exclude categories with unwanted characters in image
                                !category.image.contains("[", ignoreCase = true) &&
                                !category.image.contains("]", ignoreCase = true) &&
                                !category.image.contains("\"", ignoreCase = true) &&

                                // Only allow specific image formats
                                (category.image.endsWith("png", ignoreCase = true) ||
                                        category.image.endsWith("jpeg", ignoreCase = true) ||
                                        category.image.endsWith("jpg", ignoreCase = true))
                    }
                    .onEach {
                        Timber.tag(tag).d("getCategories: ${it.name}, ${it.image}")
                    }
                    .map {
                        it.toCategory()
                    }

                return Result.Success(categories)
            }

            is Result.Error -> {
                return categoriesResult
            }
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
        return httpClient.get<List<ProductDto>>(
            route = "/categories/$id/products"
        ).map { productsDto ->
            productsDto.map { it.toProduct() }
        }
    }


}