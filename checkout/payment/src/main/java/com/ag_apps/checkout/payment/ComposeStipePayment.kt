//package com.ag_apps.checkout.payment
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.platform.LocalContext
//import com.ag_apps.core.domain.User
//import com.stripe.android.PaymentConfiguration
//import com.stripe.android.paymentsheet.PaymentSheet
//import com.stripe.android.paymentsheet.PaymentSheetResult
//import com.stripe.android.paymentsheet.rememberPaymentSheet
//
///**
// * @author Ahmed Guedmioui
// */
//@Composable
//fun StripePayment(user: User) {
//    val paymentSheet = rememberPaymentSheet(::onPaymentSheetResult)
//    val context = LocalContext.current
//    var customerConfig by remember {
//        mutableStateOf<PaymentSheet.CustomerConfiguration?>(null)
//    }
//    var paymentIntentClientSecret by remember {
//        mutableStateOf<String?>(null)
//    }
//
//    LaunchedEffect(key1 = context, key2 = user) {
//
//        paymentIntentClientSecret = "paymentIntent"
//        customerConfig = PaymentSheet.CustomerConfiguration(
//            id = user.userId,
//            ephemeralKeySecret = "ephemeralKey"
//        )
//        val publishableKey = "publishableKey"
//        PaymentConfiguration.init(context, publishableKey)
//
//        val currentConfig = customerConfig
//        val currentClientSecret = paymentIntentClientSecret
//
//        if (currentConfig != null && currentClientSecret != null) {
//            presentPaymentSheet(paymentSheet, currentConfig, currentClientSecret)
//        }
//    }
//}
//
//private fun presentPaymentSheet(
//    paymentSheet: PaymentSheet,
//    customerConfig: PaymentSheet.CustomerConfiguration,
//    paymentIntentClientSecret: String
//) {
//    paymentSheet.presentWithPaymentIntent(
//        paymentIntentClientSecret,
//        PaymentSheet.Configuration(
//            merchantDisplayName = "My merchant name",
//            customer = customerConfig,
//            allowsDelayedPaymentMethods = true
//        )
//    )
//}
//
//private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
//    when (paymentSheetResult) {
//        is PaymentSheetResult.Canceled -> {
//            print("Canceled")
//        }
//
//        is PaymentSheetResult.Failed -> {
//            print("Error: ${paymentSheetResult.error}")
//        }
//
//        is PaymentSheetResult.Completed -> {
//            print("Completed")
//        }
//    }
//}