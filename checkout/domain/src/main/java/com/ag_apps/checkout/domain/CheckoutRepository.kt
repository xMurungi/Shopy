package com.ag_apps.checkout.domain

import com.ag_apps.core.domain.models.Card
import com.ag_apps.core.domain.models.User
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
interface CheckoutRepository {

    suspend fun getTotalPrice(): Result<Double, DataError.Network>

    suspend fun getUser(): Result<User, DataError.Network>

    suspend fun updateUser(user: User?): Result<String, DataError.Network>

    suspend fun saveCard(card: Card)

    suspend fun getCard(): Card?

    suspend fun getPaymentConfig(
        user: User,
        totalPrice: Double,
        onPaymentConfig: (PaymentConfig?) -> Unit
    )

    suspend fun submitOrder(user: User?, totalPrice: Double?)

}