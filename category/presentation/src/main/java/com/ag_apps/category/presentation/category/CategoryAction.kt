package com.ag_apps.category.presentation.category

/**
 * @author Ahmed Guedmioui
 */
sealed interface CategoryAction {
    data class LoadCategoryProducts(val categoryId: Int) : CategoryAction
    data object Refresh : CategoryAction
    data object RefreshUpdatedProducts : CategoryAction
    data object ToggleProductsLayout : CategoryAction
    data class ToggleProductInWishlist(val productIndex: Int) : CategoryAction
    data class ToggleProductInCart(val productIndex: Int) : CategoryAction
    data class ClickProduct(val productIndex: Int) : CategoryAction
    data object Search : CategoryAction
    data object Back : CategoryAction
}