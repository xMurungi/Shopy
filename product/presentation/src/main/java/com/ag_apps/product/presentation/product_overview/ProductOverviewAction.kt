package com.ag_apps.product.presentation.product_overview

/**
 * @author Ahmed Guedmioui
 */
sealed interface ProductOverviewAction {
    data object Refresh : ProductOverviewAction
    data object Paginate : ProductOverviewAction
    data object LoadProductsWithFilters : ProductOverviewAction
    data object ToggleFilter : ProductOverviewAction
    data object ToggleProductsLayout : ProductOverviewAction
    data class ToggleProductInWishlist(val productIndex: Int) : ProductOverviewAction
    data class ToggleProductInCart(val productIndex: Int) : ProductOverviewAction
    data class SelectProduct(val productIndex: Int) : ProductOverviewAction
}