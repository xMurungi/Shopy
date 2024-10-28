package com.ag_apps.wishlist.presentation

import com.ag_apps.core.domain.Product

/**
 * @author Ahmed Guedmioui
 */
data class WishlistState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val products: List<Product> = emptyList(),
)