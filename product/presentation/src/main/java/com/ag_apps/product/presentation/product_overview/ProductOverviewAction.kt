package com.ag_apps.product.presentation.product_overview

/**
 * @author Ahmed Guedmioui
 */
sealed interface ProductOverviewAction {
    data object Refresh : ProductOverviewAction
    data object RefreshUpdatedProducts : ProductOverviewAction
    data object Paginate : ProductOverviewAction
    data object ApplyFilter : ProductOverviewAction
    data object ToggleFilter : ProductOverviewAction
    data object ToggleProductsLayout : ProductOverviewAction
    data class ToggleProductInWishlist(val productIndex: Int) : ProductOverviewAction
    data class ToggleProductInCart(val productIndex: Int) : ProductOverviewAction
    data class ClickProduct(val productIndex: Int) : ProductOverviewAction
    data object Search : ProductOverviewAction
}