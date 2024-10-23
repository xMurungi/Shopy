package com.ag_apps.core.presentation.model

/**
 * @author Ahmed Guedmioui
 */
data class ProductUI(
    val productId: Int,
    val title: String,
    val description: String,
    val image: String,
    val price: Int,
    val rating: Int,
    val categoryName: String,
    val isInWishList: Boolean,
    val isInCartList: Boolean,
)
