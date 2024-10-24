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

        return Result.Success(dummyProducts)

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
                                    image.contains("\"", ignoreCase = true) &&
                                    !image.contains("png") ||
                                    !image.contains("jpeg") ||
                                    !image.contains("jpg")
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

        return Result.Success(dummyProducts)

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
        productId: Int
    ): Result<Product, DataError.Network> {

        return Result.Success(dummyProducts[0])

        return httpClient.get<ProductDto>(
            route = "/products/$productId"
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
                    .filter { category ->
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
        categoryId: Int
    ): Result<Category, DataError.Network> {
        return httpClient.get<CategoryDto>(
            route = "/categories/$categoryId"
        ).map { categoryDto ->
            categoryDto.toCategory()
        }
    }

    override suspend fun getCategoryProducts(
        categoryId: Int
    ): Result<List<Product>, DataError.Network> {

        return Result.Success(dummyProducts)

        return httpClient.get<List<ProductDto>>(
            route = "/categories/$categoryId/products"
        ).map { productsDto ->
            productsDto.map { it.toProduct() }
        }
    }
}

val dummyProducts = listOf(
    Product(
        productId = 1,
        title = "Blue Shirt with no logo and simple design",
        description = "Blue Shirt with no logo and simple design that is very comfortable and stylish for summer time and parties.",
        brand = "Zara",
        thumbnail = "https://ph-test-11.slatic.net/p/49b09ac65cd105a535c7342974984cb3.jpg",
        images = listOf(
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSuV_-Ugu4Kr6KWzCFYjMfOIfzGscAWw1d30E9tL_jnTL4otAtRzy0fnPitRzVIfThZK9I&usqp=CAU",
            "https://ph-test-11.slatic.net/p/45b6ee7725ad871cf46fc0b9be59953e.jpg",
            "https://m.media-amazon.com/images/I/71WvF2D8SfL._AC_UY1100_.jpg"
        ),
        rating = 8.5f,
        price = 59.99f,
        discount = 20,
        categoryName = "Clothes",
        filter = "Size",
        filterList = listOf("S", "M", "L", "XL"),
        isInWishList = false,
        isInCartList = false
    ),
    Product(
        productId = 2,
        title = "Product 1",
        description = "Product 1 description",
        brand = "Brand",
        thumbnail = "",
        images = emptyList(),
        rating = 8.5f,
        price = 100.35f,
        discount = 20,
        categoryName = "Category 1",
        filter = "Size",
        filterList = listOf("S", "M", "L", "XL"),
        isInWishList = false,
        isInCartList = false
    ),
    Product(
        productId = 3,
        title = "Product 1",
        description = "Product 1 description",
        brand = "Brand",
        thumbnail = "",
        images = emptyList(),
        rating = 8.5f,
        price = 100.35f,
        discount = 20,
        categoryName = "Category 1",
        filter = "Size",
        filterList = listOf("S", "M", "L", "XL"),
        isInWishList = false,
        isInCartList = false
    ),
    Product(
        productId = 4,
        title = "Product 1",
        description = "Product 1 description",
        brand = "Brand",
        thumbnail = "",
        images = emptyList(),
        rating = 8.5f,
        price = 100.35f,
        discount = 20,
        categoryName = "Category 1",
        filter = "Size",
        filterList = listOf("S", "M", "L", "XL"),
        isInWishList = false,
        isInCartList = false
    ),
    Product(
        productId = 5,
        title = "Product 1",
        description = "Product 1 description",
        brand = "Brand",
        thumbnail = "",
        images = emptyList(),
        rating = 8.5f,
        price = 100.35f,
        discount = 20,
        categoryName = "Category 1",
        filter = "Size",
        filterList = listOf("S", "M", "L", "XL"),
        isInWishList = false,
        isInCartList = false
    )
)