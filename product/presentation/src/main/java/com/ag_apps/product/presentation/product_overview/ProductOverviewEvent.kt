package com.ag_apps.product.presentation.product_overview

import com.ag_apps.core.presentation.ui.UiText

/**
 * @author Ahmed Guedmioui
 */
sealed interface ProductOverviewEvent {
    data class Error(val error: UiText) : ProductOverviewEvent
}