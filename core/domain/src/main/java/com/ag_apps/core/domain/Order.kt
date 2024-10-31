package com.ag_apps.core.domain

/**
 * @author Ahmed Guedmioui
 */
data class Order(
    val date: Long,
    val totalPrice: Double,
    val address: Address?,
    val products: Map<Int, String?>
)
