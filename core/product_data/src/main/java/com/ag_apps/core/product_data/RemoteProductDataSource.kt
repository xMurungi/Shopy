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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import timber.log.Timber
import java.io.BufferedReader

/**
 * @author Ahmed Guedmioui
 */
class RemoteProductDataSource(
    private val httpClient: HttpClient,
    private val application: Application
) : ProductDataSource {

    private val tag = "ProductDataSource"

    private var dummyCategories: List<Category>? = null
    private var dummyProducts: List<Product>? = null

    init {
        dummyProducts = loadProductsFromAssets()
        dummyCategories = loadCategoriesFromAssets()
    }

    override suspend fun getProducts(
        query: String?,
        offset: Int,
        minPrice: Int?,
        maxPrice: Int?,
    ): Result<List<Product>, DataError.Network> {

        dummyProducts?.let { dummyProducts ->
            if (offset > 0) return Result.Success(emptyList())
            return Result.Success(dummyProducts)
        }

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

        dummyProducts?.let { dummyProducts ->
            return Result.Success(dummyProducts.first { it.productId == productId })
        }

        return httpClient.get<ProductDto>(
            route = "/products/$productId"
        ).map { productDto ->
            productDto.toProduct()
        }
    }

    override suspend fun getCategories(): Result<List<Category>, DataError.Network> {

        dummyCategories?.let { dummyCategories ->
            return Result.Success(dummyCategories)
        }


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

        dummyCategories?.let { dummyCategories ->
            return Result.Success(dummyCategories.first { it.categoryId == categoryId })
        }

        return httpClient.get<CategoryDto>(
            route = "/categories/$categoryId"
        ).map { categoryDto ->
            categoryDto.toCategory()
        }
    }

    override suspend fun getCategoryProducts(
        categoryId: Int
    ): Result<List<Product>, DataError.Network> {

        dummyProducts?.let { dummyProducts ->
            return Result.Success(dummyProducts.filter { it.categoryId == categoryId })
        }

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

    private fun loadProductsFromAssets(): List<Product>? {
        println("datasourceAssets Product")
        return try {
            val inputStream = application.assets.open("Products.json")
            val bufferedReader = BufferedReader(inputStream.reader())
            val jsonText = bufferedReader.use { it.readText() }

            val listType = object : TypeToken<List<ProductDto>>() {}.type
            val products = Gson().fromJson<List<ProductDto>>(jsonText, listType)
            println("datasourceAssets Product $products")
            products.map { it.toProduct() }
        } catch (e: Exception) {
            e.printStackTrace()
            println("datasourceAssets Product null")
            null
        }
    }

    private fun loadCategoriesFromAssets(): List<Category>? {
        println("datasourceAssets Category")
        return try {
            val inputStream = application.assets.open("Categories.json")
            val bufferedReader = BufferedReader(inputStream.reader())
            val jsonText = bufferedReader.use { it.readText() }

            val listType = object : TypeToken<List<CategoryDto>>() {}.type
            val categories = Gson().fromJson<List<CategoryDto>>(jsonText, listType)

            println("datasourceAssets Category $categories")
            categories.map { it.toCategory() }
        } catch (e: Exception) {
            e.printStackTrace()
            println("datasourceAssets Category null")
            null
        }
    }
}
