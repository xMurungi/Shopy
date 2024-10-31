package com.ag_apps.cart.data

import com.ag_apps.cart.domain.CartRepository
import com.ag_apps.core.domain.Product
import com.ag_apps.core.domain.ProductDataSource
import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
class CartRepositoryImpl(
    private val productDataSource: ProductDataSource,
    private val userDataSource: UserDataSource
) : CartRepository {

    private val tag = "CartRepository: "

    override suspend fun getCartProducts(): Result<List<Product>, DataError.Network> {

        when (val userResult = userDataSource.getUser()) {
            is Result.Success -> {
                val productsForUser = mutableListOf<Product>()
                userResult.data.cart.forEach { cartProduct ->
                    when (val product = productDataSource.getProduct(cartProduct.key)) {
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
}