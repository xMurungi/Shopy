package com.ag_apps.checkout.payment

import android.app.Application
import com.ag_apps.checkout.domain.PaymentConfig
import com.ag_apps.core.domain.models.User
import com.stripe.android.PaymentConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.coroutines.cancellation.CancellationException

/**
 * @author Ahmed Guedmioui
 */
class StripeClient(
    private val client: HttpClient,
    private val application: Application
) {

    suspend fun getPaymentConfig(
        user: User,
        totalPrice: Double
    ): PaymentConfig? {

        val requestBody: Map<String, Any?> = mapOf(
            "customerId" to user.userId,
            "name" to user.name,
            "email" to user.email,
            "street" to user.address?.street,
            "city" to user.address?.city,
            "region" to user.address?.region,
            "zipCode" to user.address?.zipCode,
            "country" to user.address?.country,
            "amount" to totalPrice,
        )

        try {

            val response: HttpResponse = client.post(
                "http://192.168.10.162:4242/payment-sheet"
            )
//            {
//                if (user.orders.isEmpty()) {
//                    setBody(requestBody)
//                }
//            }

            val responseBody = response.bodyAsText()
            val responseJson = Json.decodeFromString<JsonObject>(responseBody)

            val paymentIntentClientSecret = responseJson["paymentIntent"]?.jsonPrimitive?.content
            val customerId = responseJson["customer"]?.jsonPrimitive?.content
            val ephemeralKeySecret = responseJson["ephemeralKey"]?.jsonPrimitive?.content
            val publishableKey = responseJson["publishableKey"]?.jsonPrimitive?.content

            if (paymentIntentClientSecret != null && customerId != null && ephemeralKeySecret != null && publishableKey != null) {

                PaymentConfiguration.init(application, publishableKey)

                return PaymentConfig(
                    publishableKey = publishableKey,
                    customerId = customerId,
                    ephemeralKeySecret = ephemeralKeySecret,
                    paymentIntentClientSecret = paymentIntentClientSecret
                )
            } else {
                return null
            }

        } catch (e: Exception) {
            e.printStackTrace()
            println("Payment Exception ${e.message}")
            if (e is CancellationException) throw e
            return null

        } finally {
            client.close()
        }
    }
}