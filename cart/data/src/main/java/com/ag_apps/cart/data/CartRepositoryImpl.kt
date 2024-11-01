package com.ag_apps.cart.data

import com.ag_apps.cart.domain.CartRepository
import com.ag_apps.core.domain.models.Product
import com.ag_apps.core.domain.abstractions.ProductDataSource
import com.ag_apps.core.domain.abstractions.UserDataSource
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
                userResult.data.cart.forEach { userCart ->
                    when (val product = productDataSource.getProduct(userCart.key)) {
                        is Result.Success -> {
                            productsForUser.add(product.data.copy(selectedFilter = userCart.value))
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
