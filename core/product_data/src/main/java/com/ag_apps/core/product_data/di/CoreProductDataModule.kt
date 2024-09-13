package com.ag_apps.core.product_data.di

import com.ag_apps.core.product_data.networking.HttpClientFactory
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val coreProductDataModule = module {
    single {
        HttpClientFactory().build()
    }
}