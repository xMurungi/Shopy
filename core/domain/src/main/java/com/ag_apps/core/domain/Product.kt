package com.ag_apps.core.domain

data class Product(
    val productId: Int,
    val title: String,
    val description: String,
    val images: List<String>,
    val price: Int,
    val categoryName: String,
    val isInWishList: Boolean,
    val isInCartList: Boolean,
)