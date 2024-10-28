package com.ag_apps.cart.presentation

/**
 * @author Ahmed Guedmioui
 */
sealed interface CartAction {
    data object Refresh : CartAction
    data class ToggleProductInWishlist(val productIndex: Int) : CartAction
    data class ClickProduct(val productIndex: Int) : CartAction
    data object Checkout : CartAction
}