package com.ag_apps.checkout.data

import com.ag_apps.checkout.domain.CheckoutRepository
import com.ag_apps.core.domain.Order
import com.ag_apps.core.domain.Product
import com.ag_apps.core.domain.ProductDataSource
import com.ag_apps.core.domain.User
import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
class CheckoutRepositoryImpl(
    private val productDataSource: ProductDataSource,
    private val userDataSource: UserDataSource
) : CheckoutRepository {

    override suspend fun getTotalPrice(): Result<Double, DataError.Network> {
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

                return Result.Success(productsForUser.sumOf { it.price })
            }

            is Result.Error -> {
                return userResult
            }
        }
    }

    override suspend fun getUser(): Result<User, DataError.Network> {
        return userDataSource.getUser()
    }

    override suspend fun updateUser(
        user: User?
    ): Result<String, DataError.Network> {
        if (user == null) {
            return Result.Error(DataError.Network.UNKNOWN)
        }
        return userDataSource.updateUser(user)
    }

    override suspend fun submitOrder(user: User?, totalPrice: Double?) {
        if (user != null && totalPrice != null) {
            val order = Order(
                date = System.currentTimeMillis(),
                totalPrice = totalPrice,
                address = user.address,
                products = user.cart
            )

            userDataSource.updateUser(
                user.copy(
                    cart = mapOf(),
                    orders = user.orders.plus(order)
                )
            )
        }
    }
}