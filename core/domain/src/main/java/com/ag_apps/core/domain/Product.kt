package com.ag_apps.core.domain

data class Product(
    val category: Category,
    val description: String,
    val id: Int,
    val images: List<String>,
    val price: Int,
    val title: String,
    val isInWishList: Boolean,
    val isInCartList: Boolean,
)