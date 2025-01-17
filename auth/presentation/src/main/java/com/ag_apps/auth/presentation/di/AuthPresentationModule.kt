package com.ag_apps.auth.presentation.di

import com.ag_apps.auth.presentation.login.LoginViewModel
import com.ag_apps.auth.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */

val authPresentationModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
}