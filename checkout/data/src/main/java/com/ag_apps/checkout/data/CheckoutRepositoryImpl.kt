package com.ag_apps.checkout.data

import com.ag_apps.checkout.domain.CheckoutRepository
import com.ag_apps.checkout.domain.PaymentConfig
import com.ag_apps.checkout.payment.StripeClient
import com.ag_apps.core.domain.abstractions.LocalStorageDataSource
import com.ag_apps.core.domain.abstractions.ProductDataSource
import com.ag_apps.core.domain.abstractions.UserDataSource
import com.ag_apps.core.domain.models.Card
import com.ag_apps.core.domain.models.Order
import com.ag_apps.core.domain.models.Product
import com.ag_apps.core.domain.models.User
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
class CheckoutRepositoryImpl(
    private val productDataSource: ProductDataSource,
    private val userDataSource: UserDataSource,
    private val localStorageDataSource: LocalStorageDataSource,
    private val client: StripeClient
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

    override suspend fun saveCard(card: Card) {
        localStorageDataSource.set(key = "nameOnCard", value = card.nameOnCard)
        localStorageDataSource.set(key = "cardNumber", value = card.cardNumber)
        localStorageDataSource.set(key = "expireDate", value = card.expireDate)
        localStorageDataSource.set(key = "cvv", value = card.cvv)
    }

    override suspend fun getCard(): Card? {
        val nameOnCard = localStorageDataSource.get("nameOnCard")
        val cardNumber = localStorageDataSource.get("cardNumber")
        val expireDate = localStorageDataSource.get("expireDate")
        val cvv = localStorageDataSource.get("cvv")

        if (nameOnCard == null || cardNumber == null || expireDate == null || cvv == null) {
            return null

        }
        return Card(
            nameOnCard = nameOnCard,
            cardNumber = cardNumber,
            expireDate = expireDate,
            cvv = cvv
        )
    }

    override suspend fun getPaymentConfig(
        user: User,
        totalPrice: Double
    ): PaymentConfig? {
        return client.getPaymentConfig(user, totalPrice)
    }

    override suspend fun submitOrder(user: User?, totalPrice: Double?) {
        if (user != null && totalPrice != null) {
            val order = Order(
                orderId = user.orders.size + 1,
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