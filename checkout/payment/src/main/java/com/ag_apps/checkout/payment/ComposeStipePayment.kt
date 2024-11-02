package com.ag_apps.checkout.payment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.ag_apps.checkout.domain.PaymentConfig
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Error
import com.ag_apps.core.domain.util.Result
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

/**
 * @author Ahmed Guedmioui
 */


@Composable
fun PaymentSheet(
    paymentConfig: PaymentConfig,
    onPaymentResult: (Result<Unit, Error>) -> Unit
) {

    println(
        "Payment PaymentSheet ${paymentConfig.customerId}"
    )


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

    val customerConfig = PaymentSheet.CustomerConfiguration(
        id = paymentConfig.customerId,
        ephemeralKeySecret = paymentConfig.ephemeralKeySecret
    )

    LaunchedEffect(true) {
        presentPaymentSheet(
            paymentSheet = paymentSheet,
            merchantDisplayName = "Checkout",
            customerConfig = customerConfig,
            paymentIntentClientSecret = paymentConfig.paymentIntentClientSecret
        )
    }
}

fun presentPaymentSheet(
    paymentSheet: PaymentSheet,
    merchantDisplayName: String,
    customerConfig: PaymentSheet.CustomerConfiguration,
    paymentIntentClientSecret: String
) {
    println("Payment presentPaymentSheet")

    paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        PaymentSheet.Configuration(
            merchantDisplayName = merchantDisplayName,
            customer = customerConfig,
            allowsDelayedPaymentMethods = true
        )
    )
}

