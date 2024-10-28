package com.ag_apps.cart.data.di

import com.ag_apps.cart.data.CartRepositoryImpl
import com.ag_apps.cart.domain.CartRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val cartDataModule = module {
    singleOf(::CartRepositoryImpl).bind<CartRepository>()
}