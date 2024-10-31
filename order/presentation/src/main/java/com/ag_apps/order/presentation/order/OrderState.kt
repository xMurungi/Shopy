package com.ag_apps.order.presentation.order

import com.ag_apps.core.domain.Order

/**
 * @author Ahmed Guedmioui
 */
data class OrderState(
    val orders: Order? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
