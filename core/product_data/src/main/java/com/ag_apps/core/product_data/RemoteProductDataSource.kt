package com.ag_apps.core.product_data

import android.app.Application
import com.ag_apps.core.data.get
import com.ag_apps.core.domain.models.Category
import com.ag_apps.core.domain.models.Product
import com.ag_apps.core.domain.abstractions.ProductDataSource
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
    private val httpClient: HttpClient,
    private val application: Application
) : ProductDataSource {

    private val tag = "ProductDataSource"

    private fun isTitleValid(title: String): Boolean {
        return !title.contains("New", ignoreCase = true) &&
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


    override suspend fun getProducts(
        query: String?,
        offset: Int,
        minPrice: Int?,
        maxPrice: Int?,
    ): Result<List<Product>, DataError.Network> {

//        if (offset > 0) return Result.Success(emptyList())
//        return Result.Success(dummyProducts)

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

        val productsResult = httpClient.get<List<ProductDto>>(
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

//        return Result.Success(dummyProducts[0])

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
        return httpClient.get<CategoryDto>(
            route = "/categories/$categoryId"
        ).map { categoryDto ->
            categoryDto.toCategory()
        }
    }

    override suspend fun getCategoryProducts(
        categoryId: Int
    ): Result<List<Product>, DataError.Network> {

//        return Result.Success(dummyProducts)

        val productsResult = httpClient.get<List<ProductDto>>(
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
        price = 59.99,
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
        price = 100.35,
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
        price = 100.35,
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
        price = 100.35,
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
        price = 100.35,
        discount = 20,
        categoryName = "Category 1",
        filter = "Size",
        filterList = listOf("S", "M", "L", "XL"),
        isInWishList = false,
        isInCartList = false
    )
)