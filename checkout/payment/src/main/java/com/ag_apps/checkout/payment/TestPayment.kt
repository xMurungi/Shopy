package com.ag_apps.checkout.payment

/**
 * @author Ahmed Guedmioui
 */

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.ag_apps.core.domain.models.User
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Error
import com.ag_apps.core.domain.util.Result
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun TestPayment(
    user: User,
    totalPrice: Double,
    onPaymentResult: (Result<Unit, Error>) -> Unit
) {

    println("Payment TestPayment")

    val paymentSheet = rememberPaymentSheet(
        paymentResultCallback = { paymentSheetResult ->
            when (paymentSheetResult) {
                is PaymentSheetResult.Canceled -> {
                    println("Payment Canceled")
                    onPaymentResult(Result.Error(DataError.Network.CANCELED))
                }

                is PaymentSheetResult.Failed -> {
                    println("Payment Error: ${paymentSheetResult.error}")
                    onPaymentResult(Result.Error(DataError.Network.PAYMENT_FAILED))
                }

                is PaymentSheetResult.Completed -> {
                    println("Payment Completed")
                    onPaymentResult(Result.Success(Unit))
                }
            }
        }
    )

    val context = LocalContext.current
    var customerConfig by remember {
        mutableStateOf<PaymentSheet.CustomerConfiguration?>(null)
    }
    val paymentIntentClientSecret by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(context) {

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

        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }

        try {
            val response: HttpResponse = client.post(
                "http://192.168.10.162:4242/payment-sheet"
            ) {
                setBody(requestBody)
            }

            println("Payment TestPayment post")

            val responseBody = response.bodyAsText()
            val responseJson = Json.decodeFromString<JsonObject>(responseBody)

            val customerId = responseJson["customer"]?.jsonPrimitive?.content
            val ephemeralKeySecret = responseJson["ephemeralKey"]?.jsonPrimitive?.content
            val publishableKey = responseJson["publishableKey"]?.jsonPrimitive?.content

            if (customerId != null && ephemeralKeySecret != null && publishableKey != null) {
                customerConfig = PaymentSheet.CustomerConfiguration(
                    id = customerId,
                    ephemeralKeySecret = ephemeralKeySecret
                )
                PaymentConfiguration.init(context, publishableKey)

                if (customerConfig != null && paymentIntentClientSecret != null) {
                    presentPaymentSheet(paymentSheet, customerConfig!!, paymentIntentClientSecret!!)
                }
            } else {
                println("Payment TestPayment Error: $customerId, $ephemeralKeySecret, $publishableKey")
                onPaymentResult(Result.Error(DataError.Network.PAYMENT_FAILED))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            println("Payment TestPayment Exception ${e.message}")
            if (e is CancellationException) throw e
            onPaymentResult(Result.Error(DataError.Network.PAYMENT_FAILED))

        }
    }

}

private fun presentPaymentSheet(
    paymentSheet: PaymentSheet,
    customerConfig: PaymentSheet.CustomerConfiguration,
    paymentIntentClientSecret: String
) {
    println("Payment TestPayment presentPaymentSheet")

    paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        PaymentSheet.Configuration(
            merchantDisplayName = "Payment Sheet",
            customer = customerConfig,
            allowsDelayedPaymentMethods = true
        )
    )
}

