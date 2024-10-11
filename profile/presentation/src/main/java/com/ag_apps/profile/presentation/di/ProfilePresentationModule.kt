package com.ag_apps.profile.presentation.di

import com.ag_apps.profile.presentation.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val profilePresentationModule = module {
    viewModel { ProfileViewModel(get()) }
}