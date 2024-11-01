package com.ag_apps.order.presentation.order

/**
 * @author Ahmed Guedmioui
 */
sealed interface OrderAction {
    data class LoadOrder(val orderId: Int) : OrderAction
    data class OnProductClick(val productId: Int) : OrderAction
    data object OnBackClick : OrderAction
}