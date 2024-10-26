package com.ag_apps.category.presentation.category_overview

/**
 * @author Ahmed Guedmioui
 */
sealed interface CategoryOverviewAction {
    data class ClickCategory(val categoryIndex: Int) : CategoryOverviewAction
    data object Search : CategoryOverviewAction
}