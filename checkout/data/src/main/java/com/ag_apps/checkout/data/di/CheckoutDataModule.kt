package com.ag_apps.checkout.data.di

import com.ag_apps.checkout.data.CheckoutRepositoryImpl
import com.ag_apps.checkout.domain.CheckoutRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */

val checkoutDataModule = module {
    singleOf(::CheckoutRepositoryImpl).bind<CheckoutRepository>()
}




















