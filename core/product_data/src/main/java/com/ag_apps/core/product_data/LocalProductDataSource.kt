package com.ag_apps.core.product_data

import android.app.Application
import com.ag_apps.core.domain.abstractions.ProductDataSource
import com.ag_apps.core.domain.models.Category
import com.ag_apps.core.domain.models.Product
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.core.product_data.dto.CategoryDto
import com.ag_apps.core.product_data.dto.ProductDto
import com.ag_apps.core.product_data.dto.toCategory
import com.ag_apps.core.product_data.dto.toProduct
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import java.io.BufferedReader

/**
 * @author Ahmed Guedmioui
 */
class LocalProductDataSource(
    private val client: HttpClient,
    private val application: Application,
) : ProductDataSource {


    private var dummyCategories: List<Category>? = null
    private var dummyProducts: List<Product>? = null

    init {
        dummyProducts = loadProductsFromAssets()
        dummyCategories = loadCategoriesFromAssets()
    }


    override suspend fun wakeupPaymentServer() {
        client.get(BuildConfig.PAYMENTS_SERVER_BASE_URL + "/wakeup")
    }

    override suspend fun getProducts(
        query: String?,
        offset: Int,
        minPrice: Int?,
        maxPrice: Int?
    ): Result<List<Product>, DataError.Network> {
        dummyProducts?.let { dummyProducts ->
            if (offset > 0) return Result.Success(emptyList())

            if (query != null) {
                return Result.Success(dummyProducts.filter {
                    it.title.contains(
                        query,
                        ignoreCase = true
                    )
                })
            }

            return Result.Success(dummyProducts)
        }

        return Result.Error(DataError.Network.UNKNOWN)
    }

    override suspend fun getProduct(
        productId: Int
    ): Result<Product, DataError.Network> {
        dummyProducts?.let { dummyProducts ->
            return Result.Success(dummyProducts.first { it.productId == productId })
        }

        return Result.Error(DataError.Network.UNKNOWN)
    }

    override suspend fun getCategories(): Result<List<Category>, DataError.Network> {
        dummyCategories?.let { dummyCategories ->
            return Result.Success(dummyCategories)
        }

        return Result.Error(DataError.Network.UNKNOWN)
    }

    override suspend fun getCategory(
        categoryId: Int
    ): Result<Category, DataError.Network> {

        dummyCategories?.let { dummyCategories ->
            return Result.Success(dummyCategories.first { it.categoryId == categoryId })
        }

        return Result.Error(DataError.Network.UNKNOWN)
    }

    override suspend fun getCategoryProducts(
        categoryId: Int
    ): Result<List<Product>, DataError.Network> {
        dummyProducts?.let { dummyProducts ->
            return Result.Success(dummyProducts.filter { it.categoryId == categoryId })
        }

        return Result.Error(DataError.Network.UNKNOWN)
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
            products.map { it.toProduct() }.shuffled()
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
            categories.map { it.toCategory() }.shuffled()
        } catch (e: Exception) {
            e.printStackTrace()
            println("datasourceAssets Category null")
            null
        }
    }
}
