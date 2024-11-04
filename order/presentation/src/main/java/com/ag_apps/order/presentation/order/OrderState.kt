package com.ag_apps.order.presentation.order

import com.ag_apps.core.domain.models.Order
import com.ag_apps.core.domain.models.Product

/**
 * @author Ahmed Guedmioui
 */
data class OrderState(
    val orderId: Int? = null,
    val order: Order? = null,
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
