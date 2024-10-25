package com.ag_apps.search.presentation

/**
 * @author Ahmed Guedmioui
 */
sealed interface SearchAction {
    data class RefreshUpdatedProductFromDetails(val updatedProductId: Int) : SearchAction
    data object Refresh : SearchAction
    data object Paginate : SearchAction
    data object ApplyFilter : SearchAction
    data object ToggleFilter : SearchAction
    data object ToggleProductsLayout : SearchAction
    data class ToggleProductInWishlist(val productIndex: Int) : SearchAction
    data class ToggleProductInCart(val productIndex: Int) : SearchAction
    data class ClickProduct(val productIndex: Int) : SearchAction
}