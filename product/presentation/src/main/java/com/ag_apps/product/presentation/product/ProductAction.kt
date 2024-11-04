package com.ag_apps.product.presentation.product

/**
 * @author Ahmed Guedmioui
 */
sealed interface ProductAction {
    data class LoadProduct(val productId: Int) : ProductAction
    data class SelectFilter(val selectedFilter: String) : ProductAction
    data object GoBack : ProductAction
    data object ToggleProductInWishlist : ProductAction
    data object ToggleProductInCart : ProductAction
    data object Refresh : ProductAction
}