package com.ag_apps.checkout.domain

/**
 * @author Ahmed Guedmioui
 */
data class PaymentConfig(
    val publishableKey: String,
    val customerId: String,
    val ephemeralKeySecret: String,
    val paymentIntentClientSecret: String
 )
