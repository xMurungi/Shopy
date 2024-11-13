package com.ag_apps.core.product_data

import com.ag_apps.core.data.get
import com.ag_apps.core.domain.abstractions.ProductDataSource
import com.ag_apps.core.domain.models.Category
import com.ag_apps.core.domain.models.Product
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.core.domain.util.map
import com.ag_apps.core.product_data.dto.CategoryDto
import com.ag_apps.core.product_data.dto.ProductDto
import com.ag_apps.core.product_data.dto.toCategory
import com.ag_apps.core.product_data.dto.toProduct
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import timber.log.Timber

/**
 * @author Ahmed Guedmioui
 */
class RemoteProductDataSource(
    private val client: HttpClient,
) : ProductDataSource {

    private val tag = "ProductDataSource"

    // only needed because Render where the server is deployed,
    // delays the very first request after not being active for a while.
    // if I deployed the server somewhere else it would not be needed.
    override suspend fun wakeupPaymentServer() {
       client.get(BuildConfig.PAYMENTS_SERVER_BASE_URL + "/wakeup")
    }

    override suspend fun getProducts(
        query: String?,
        offset: Int,
        minPrice: Int?,
        maxPrice: Int?,
    ): Result<List<Product>, DataError.Network> {

        Timber.tag(tag).d(
            "getProducts: query: $query, offset: $offset, minPrice: $minPrice, maxPrice: $maxPrice"
        )

        val queryParameters: MutableMap<String, Any> = mutableMapOf(
            "offset" to offset,
            "limit" to 10
        )

        if (minPrice != null) {
            queryParameters["price_min"] = minPrice
        }
        if (maxPrice != null) {
            queryParameters["price_max"] = maxPrice
        }
        if (query != null) {
            queryParameters["title"] = query
        }

        val productsResult = client.get<List<ProductDto>>(
            route = "/products",
            queryParameters = queryParameters
        )

        when (productsResult) {
            is Result.Success -> {

                val products = productsResult.data
                    .filter { product ->
                        isTitleValid(product.title) && product.images.all { isImageValid(it) }
                    }
                    .onEach {
                        Timber.tag(tag).d("getProducts: ${it.title}, ${it.images}")
                    }
                    .map { it.toProduct() }

                return Result.Success(products)
            }

            is Result.Error -> {
                return Result.Error(productsResult.error)
            }
        }
    }

    override suspend fun getProduct(
        productId: Int
    ): Result<Product, DataError.Network> {

        return client.get<ProductDto>(
            route = "/products/$productId"
        ).map { productDto ->
            productDto.toProduct()
        }
    }

    override suspend fun getCategories(): Result<List<Category>, DataError.Network> {

        val categoriesResult = client.get<List<CategoryDto>>(
            route = "/categories"
        )

        when (categoriesResult) {
            is Result.Success -> {
                val categories = categoriesResult.data
                    .shuffled()
                    .filter { category ->
                        isTitleValid(category.name) && isImageValid(category.image)
                    }
                    .onEach {
                        Timber.tag(tag).d("getCategories: ${it.name}, ${it.image}")
                    }
                    .reversed()
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
        categoryId: Int
    ): Result<Category, DataError.Network> {

        return client.get<CategoryDto>(
            route = "/categories/$categoryId"
        ).map { categoryDto ->
            categoryDto.toCategory()
        }
    }

    override suspend fun getCategoryProducts(
        categoryId: Int
    ): Result<List<Product>, DataError.Network> {

        val productsResult = client.get<List<ProductDto>>(
            route = "/categories/$categoryId/products"
        )

        when (productsResult) {
            is Result.Success -> {

                val products = productsResult.data
                    .filter { product ->
                        isTitleValid(product.title) && product.images.all { isImageValid(it) }
                    }
                    .onEach {
                        Timber.tag(tag).d("getProducts: ${it.title}, ${it.images}")
                    }
                    .map { it.toProduct() }

                return Result.Success(products)
            }

            is Result.Error -> {
                return Result.Error(productsResult.error)
            }
        }

    }

    private fun isTitleValid(title: String): Boolean {
        return !title.contains("New", ignoreCase = true) &&
                !title.contains("String", ignoreCase = true) &&
                !title.contains("Name", ignoreCase = true) &&
                !title.contains("Category", ignoreCase = true) &&
                !title.contains("Product", ignoreCase = true) &&
                !title.contains("Change", ignoreCase = true) &&
                !title.contains("Title", ignoreCase = true)
    }

    private fun isImageValid(image: String): Boolean {
        return !(image.contains("[") ||
                image.contains("]") ||
                image.contains("\"")) &&
                (image.contains("png", ignoreCase = true) ||
                        image.contains("jpeg", ignoreCase = true) ||
                        image.contains("jpg", ignoreCase = true))
    }
}
