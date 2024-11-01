package com.ag_apps.product.data

import com.ag_apps.core.domain.models.Category
import com.ag_apps.core.domain.models.Product
import com.ag_apps.core.domain.abstractions.ProductDataSource
import com.ag_apps.core.domain.abstractions.UserDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.product.domain.ProductRepository

/**
 * @author Ahmed Guedmioui
 */
class ProductRepositoryImpl(
    private val productDataSource: ProductDataSource,
    private val userDataSource: UserDataSource
) : ProductRepository {

    private val tag = "ProductRepository: "

    override suspend fun getProducts(
        offset: Int,
        minPrice: Int?,
        maxPrice: Int?,
    ): Result<List<Product>, DataError.Network> {

        val userResult = userDataSource.getUser()

        val productsResult = productDataSource.getProducts(
            offset = offset,
            minPrice = minPrice,
            maxPrice = maxPrice,
        )

        if (userResult is Result.Success && productsResult is Result.Success) {
            val productsForUser = productsResult.data.map { product ->
                if (userResult.data.wishlist.contains(product.productId)) {
                    if (userResult.data.cart.contains(product.productId)) {
                        product.copy(isInCartList = true, isInWishList = true)
                    } else {
                        product.copy(isInWishList = true)
                    }
                } else if (userResult.data.cart.contains(product.productId)) {
                    product.copy(isInCartList = true)
                } else {
                    product
                }
            }

            return Result.Success(productsForUser)
        }

        return productsResult

    }

    override suspend fun getProduct(
        productId: Int
    ): Result<Product, DataError.Network> {
        val userResult = userDataSource.getUser()
        val productResult = productDataSource.getProduct(productId)

        if (userResult is Result.Success && productResult is Result.Success) {
            var product = productResult.data

            if (userResult.data.wishlist.contains(product.productId)) {
                product = product.copy(isInWishList = true)
            }

            if (userResult.data.cart.contains(product.productId)) {
                product = product.copy(
                    isInCartList = true,
                    selectedFilter = userResult.data.cart.getValue(product.productId)
                )
            }

            return Result.Success(product)
        }

        return productResult
    }

    override suspend fun addProductToWishlist(
        productId: Int
    ): Result<Unit, DataError.Network> {
        return userDataSource.addProductToWishlist(productId)
    }

    override suspend fun removeProductFromWishlist(
        productId: Int
    ): Result<Unit, DataError.Network> {
        return userDataSource.removeProductFromWishlist(productId)
    }

    override suspend fun addProductToCart(
        productId: Int, filter: String?
    ): Result<Unit, DataError.Network> {
        return userDataSource.addProductToCart(productId, filter)
    }

    override suspend fun removeProductFromCart(
        productId: Int
    ): Result<Unit, DataError.Network> {
        return userDataSource.removeProductFromCart(productId)
    }

    override suspend fun getCategories(): Result<List<Category>, DataError.Network> {
        return productDataSource.getCategories()
    }
}
