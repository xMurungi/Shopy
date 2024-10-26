package com.ag_apps.category.presentation.di

import com.ag_apps.category.presentation.category.CategoryViewModel
import com.ag_apps.category.presentation.category_overview.CategoryOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val categoryPresentationModule = module {
    viewModel { CategoryOverviewViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
}