package com.ag_apps.wishlist.data.di

import com.ag_apps.wishlist.data.WishlistRepositoryImpl
import com.ag_apps.wishlist.domain.WishlistRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val wishlistDataModule = module {
    singleOf(::WishlistRepositoryImpl).bind<WishlistRepository>()
}