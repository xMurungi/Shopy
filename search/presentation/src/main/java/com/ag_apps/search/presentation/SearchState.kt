package com.ag_apps.search.presentation

import androidx.compose.foundation.text.input.TextFieldState
import com.ag_apps.core.domain.Category
import com.ag_apps.core.domain.Product

/**
 * @author Ahmed Guedmioui
 */
data class SearchState(
    val isLoading: Boolean = false,
    val isApplyingFilter: Boolean = false,
    val searchQueryState: TextFieldState = TextFieldState(""),
    val isError: Boolean = false,
    val products: List<Product> = emptyList(),
    val productsOffset: Int = 0,
    val minPriceState: TextFieldState = TextFieldState(""),
    val maxPriceState: TextFieldState = TextFieldState(""),
    val isGridLayout: Boolean = true,
    val isFilterOpen: Boolean = false
)
