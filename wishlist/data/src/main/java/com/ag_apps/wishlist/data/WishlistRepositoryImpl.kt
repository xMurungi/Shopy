package com.ag_apps.wishlist.data

import com.ag_apps.core.domain.models.Product
import com.ag_apps.core.domain.abstractions.ProductDataSource
import com.ag_apps.core.domain.abstractions.UserDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.wishlist.domain.WishlistRepository

/**
 * @author Ahmed Guedmioui
 */
class WishlistRepositoryImpl(
    private val productDataSource: ProductDataSource,
    private val userDataSource: UserDataSource
) : WishlistRepository {

    private val tag = "WishlistRepository: "

    override suspend fun getWishlistProducts(): Result<List<Product>, DataError.Network> {

        when (val userResult = userDataSource.getUser()) {
            is Result.Success -> {
                val productsForUser = mutableListOf<Product>()
                userResult.data.wishlist.forEach { productId ->
                    when (val product = productDataSource.getProduct(productId)) {
                        is Result.Success -> {
                            productsForUser.add(product.data)
                        }

                        is Result.Error -> Unit
                    }
                }

                return Result.Success(productsForUser)
            }

            is Result.Error -> {
                return userResult
            }
        }
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
}
