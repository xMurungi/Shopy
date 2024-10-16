package com.ag_apps.product.presentation

import com.ag_apps.core.domain.Product

/**
 * @author Ahmed Guedmioui
 */
data class ProductState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val products: List<Product> = emptyList(),
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
)