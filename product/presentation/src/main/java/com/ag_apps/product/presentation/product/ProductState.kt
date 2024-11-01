package com.ag_apps.product.presentation.product

import com.ag_apps.core.domain.models.Product

/**
 * @author Ahmed Guedmioui
 */
data class ProductState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val product: Product? = null,
    val selectedFilter: String? = null,
    val isProductUpdate: Boolean = false
)