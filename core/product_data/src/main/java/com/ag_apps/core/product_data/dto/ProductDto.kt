package com.ag_apps.core.product_data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val category: CategoryDto,
    val description: String,
    val id: Int,
    val images: List<String>,
    val price: Int,
    val title: String
)