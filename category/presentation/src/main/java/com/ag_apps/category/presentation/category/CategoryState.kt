package com.ag_apps.category.presentation.category

import com.ag_apps.core.domain.models.Category
import com.ag_apps.core.domain.models.Product

/**
 * @author Ahmed Guedmioui
 */
data class CategoryState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val categoryId: Int? = null,
    val category: Category? = null,
    val products: List<Product> = emptyList(),
    val isGridLayout: Boolean = true,
)