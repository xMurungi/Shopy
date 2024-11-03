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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.coroutines.cancellation.CancellationException

/**
 * @author Ahmed Guedmioui
 */
class StripeClient(
    private val client: HttpClient,
    private val application: Application,
    private val applicationScope: CoroutineScope,
) {

    fun getPaymentConfig(
        user: User,
        totalPrice: Double,
        onPaymentConfig: (PaymentConfig?) -> Unit
    ) {
        println("Payment getPaymentConfig")

        applicationScope.launch {
            val requestBody: HashMap<String, Any?> = if (user.orders.isEmpty()) {
                hashMapOf(
                    "customerId" to user.customerId,
                    "name" to user.name,
                    "email" to user.email,
                    "street" to user.address?.street,
                    "city" to user.address?.city,
                    "region" to user.address?.region,
                    "zipCode" to user.address?.zipCode,
                    "country" to user.address?.country,
                    "amount" to (totalPrice * 100).toInt().toString(),
                )
            } else {
                hashMapOf(
                    "customerId" to user.customerId,
                    "amount" to (totalPrice * 100).toInt().toString(),
                )
            }

            try {

                val response: HttpResponse = client.post(
                    "http://192.168.10.162:4242/payment-sheet"
                ) {
                    setBody(requestBody)
                }
                println("Payment getPaymentConfig post")

                val responseBody = response.bodyAsText()
                val responseJson = Json.decodeFromString<JsonObject>(responseBody)

                val paymentIntentClientSecret =
                    responseJson["paymentIntent"]?.jsonPrimitive?.content
                val customerId = responseJson["customer"]?.jsonPrimitive?.content
                val ephemeralKeySecret = responseJson["ephemeralKey"]?.jsonPrimitive?.content
                val publishableKey = responseJson["publishableKey"]?.jsonPrimitive?.content


                println("Payment getPaymentConfig response: customerId = $customerId")

                if (paymentIntentClientSecret != null && customerId != null && ephemeralKeySecret != null && publishableKey != null) {

                    PaymentConfiguration.init(application, publishableKey)
                    println("Payment getPaymentConfig PaymentConfiguration.init")

                    onPaymentConfig(
                        PaymentConfig(
                            publishableKey = publishableKey,
                            customerId = customerId,
                            ephemeralKeySecret = ephemeralKeySecret,
                            paymentIntentClientSecret = paymentIntentClientSecret
                        )
                    )
                } else {
                    onPaymentConfig(null)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                println("Payment Exception ${e.message}")
                if (e is CancellationException) throw e
                onPaymentConfig(null)

            }
        }
    }
}