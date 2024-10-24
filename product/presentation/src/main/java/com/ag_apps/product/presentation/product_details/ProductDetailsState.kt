package com.ag_apps.product.presentation.product_details

import com.ag_apps.core.domain.Product

/**
 * @author Ahmed Guedmioui
 */
data class ProductDetailsState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val product: Product? = null,
    val selectedFilterIndex: Int? = null
)