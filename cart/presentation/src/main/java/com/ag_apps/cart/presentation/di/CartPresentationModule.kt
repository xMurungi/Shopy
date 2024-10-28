package com.ag_apps.cart.presentation.di

import com.ag_apps.cart.presentation.CartViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */

val cartPresentationModule = module {
    viewModel { CartViewModel(get()) }
}