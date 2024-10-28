package com.ag_apps.wishlist.presentation

/**
 * @author Ahmed Guedmioui
 */
sealed interface WishlistAction {
    data object Refresh : WishlistAction
    data class RemoveProductFromWishlist(val productIndex: Int) : WishlistAction
    data class ToggleProductInCart(val productIndex: Int) : WishlistAction
    data class ClickProduct(val productIndex: Int) : WishlistAction
    data object Search : WishlistAction
}