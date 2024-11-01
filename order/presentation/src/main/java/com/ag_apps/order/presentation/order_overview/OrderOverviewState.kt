package com.ag_apps.order.presentation.order_overview

import com.ag_apps.core.domain.models.Order

/**
 * @author Ahmed Guedmioui
 */
data class OrderOverviewState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
