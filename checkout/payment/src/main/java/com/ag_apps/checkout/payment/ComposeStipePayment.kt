package com.ag_apps.checkout.payment

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ag_apps.checkout.domain.PaymentConfig
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Error
import com.ag_apps.core.domain.util.Result
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.PaymentSheetResultCallback
import com.stripe.android.paymentsheet.rememberPaymentSheet

/**
 * @author Ahmed Guedmioui
 */


@Composable
fun PaymentSheet(
    paymentConfig: PaymentConfig,
    onPaymentResult: (Result<Unit, Error>) -> Unit
){

    println("Payment PaymentSheet")

    val paymentResultCallback = PaymentSheetResultCallback { paymentSheetResult ->
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

    val paymentSheet = rememberPaymentSheet(
        paymentResultCallback = paymentResultCallback
    )
    presentPaymentSheet(
        paymentSheet = paymentSheet,
        merchantDisplayName =  stringResource(R.string.checkout_your_order),
        paymentConfig = paymentConfig
    )
}

private fun presentPaymentSheet(
    paymentSheet: PaymentSheet,
    merchantDisplayName: String,
    paymentConfig: PaymentConfig
) {
    println("Payment presentPaymentSheet")

   val customerConfig = PaymentSheet.CustomerConfiguration(
        id = paymentConfig.customerId,
        ephemeralKeySecret = paymentConfig.ephemeralKeySecret
    )

    paymentSheet.presentWithPaymentIntent(
        paymentConfig.paymentIntentClientSecret,
        PaymentSheet.Configuration(
            merchantDisplayName = merchantDisplayName,
            customer = customerConfig,
            allowsDelayedPaymentMethods = true
        )
    )
}

