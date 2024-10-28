package com.ag_apps.category.data

import com.ag_apps.category.domain.CategoryRepository
import com.ag_apps.core.domain.Category
import com.ag_apps.core.domain.Product
import com.ag_apps.core.domain.ProductDataSource
import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
class CategoryRepositoryImpl(
    private val productDataSource: ProductDataSource,
    private val userDataSource: UserDataSource
) : CategoryRepository {

    private val tag = "CategoryRepository: "

    override suspend fun getCategoryProducts(
        categoryId: Int,
    ): Result<List<Product>, DataError.Network> {

        val userResult = userDataSource.getUser()

        val productsResult = productDataSource.getCategoryProducts(
            categoryId = categoryId
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

    override suspend fun getCategory(categoryId: Int): Result<Category, DataError.Network> {
        return productDataSource.getCategory(categoryId)
    }
}
