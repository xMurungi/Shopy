package com.ag_apps.order.presentation.di

import com.ag_apps.order.presentation.order_overview.OrderOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val orderPresentationModule = module {
    viewModel { OrderOverviewViewModel(get()) }
}