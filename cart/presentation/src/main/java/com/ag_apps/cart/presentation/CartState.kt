package com.ag_apps.cart.presentation

import com.ag_apps.core.domain.models.Product

/**
 * @author Ahmed Guedmioui
 */
data class CartState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val products: List<Product> = emptyList(),
    val totalPrice: Double = 0.0
)