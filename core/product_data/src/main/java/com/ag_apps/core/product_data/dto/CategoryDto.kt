package com.ag_apps.core.product_data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: Int,
    val image: String,
    val name: String
)