package com.ag_apps.order.presentation.order_overview

/**
 * @author Ahmed Guedmioui
 */
sealed interface OrderOverviewAction {
    data class OnOrderClick(val orderId: Int) : OrderOverviewAction
    data object Refresh : OrderOverviewAction
    data object OnBackClick : OrderOverviewAction
}