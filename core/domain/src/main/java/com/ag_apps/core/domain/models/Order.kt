package com.ag_apps.core.domain.models

/**
 * @author Ahmed Guedmioui
 */
data class Order(
    val orderId: Int,
    val date: Long,
    val totalPrice: Double,
    val address: Address?,
    val products: Map<Int, String?>
)
