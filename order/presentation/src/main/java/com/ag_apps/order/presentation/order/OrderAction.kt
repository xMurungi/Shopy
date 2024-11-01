package com.ag_apps.order.presentation.order

/**
 * @author Ahmed Guedmioui
 */
sealed interface OrderAction {
    data class LoadOrder(val orderId: Int) : OrderAction
    data class OnProductClick(val productId: Int) : OrderAction
    data class ToggleProductInWishlist(val productIndex: Int) : OrderAction
    data class ToggleProductInCart(val productIndex: Int) : OrderAction
    data object OnBackClick : OrderAction
}