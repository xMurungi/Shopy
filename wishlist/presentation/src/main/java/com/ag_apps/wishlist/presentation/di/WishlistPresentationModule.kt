package com.ag_apps.wishlist.presentation.di

import com.ag_apps.wishlist.presentation.WishlistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */

val wishlistPresentationModule = module {
    viewModel { WishlistViewModel(get()) }
}