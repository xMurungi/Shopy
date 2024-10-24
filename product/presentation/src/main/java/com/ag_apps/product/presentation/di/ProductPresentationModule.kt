package com.ag_apps.product.presentation.di

import com.ag_apps.product.presentation.product_details.ProductDetailsViewModel
import com.ag_apps.product.presentation.product_overview.ProductOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val productPresentationModule = module {
    viewModel { ProductOverviewViewModel(get()) }
    viewModel { ProductDetailsViewModel(get()) }
}