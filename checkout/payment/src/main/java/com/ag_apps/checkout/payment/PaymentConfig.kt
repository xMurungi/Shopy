package com.ag_apps.checkout.payment

/**
 * @author Ahmed Guedmioui
 */
data class PaymentConfig(
    val publishableKey: String,
    val ephemeralKeySecret: String,
    val paymentIntentClientSecret: String
)
