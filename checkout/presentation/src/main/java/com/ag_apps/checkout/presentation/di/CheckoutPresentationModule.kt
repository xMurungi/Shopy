package com.ag_apps.checkout.presentation.di

import com.ag_apps.checkout.presentation.checkout.CheckoutViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val checkoutPresentationModule = module {
    viewModel { CheckoutViewModel(get()) }
}