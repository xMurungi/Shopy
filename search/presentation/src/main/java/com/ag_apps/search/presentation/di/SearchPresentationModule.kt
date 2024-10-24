package com.ag_apps.search.presentation.di

import com.ag_apps.search.presentation.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val searchPresentationModule = module {
    viewModel { SearchViewModel(get()) }
}