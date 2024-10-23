package com.ag_apps.product.presentation.product_details

/**
 * @author Ahmed Guedmioui
 */
sealed interface ProductDetailsAction {
    data class LoadProduct(val productId: Int) : ProductDetailsAction
    data object ToggleProductInWishlist : ProductDetailsAction
    data object ToggleProductInCart : ProductDetailsAction
}