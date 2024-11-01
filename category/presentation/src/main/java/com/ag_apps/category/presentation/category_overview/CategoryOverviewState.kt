package com.ag_apps.category.presentation.category_overview

import com.ag_apps.core.domain.models.Category

/**
 * @author Ahmed Guedmioui
 */
data class CategoryOverviewState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val categories: List<Category> = emptyList()
)