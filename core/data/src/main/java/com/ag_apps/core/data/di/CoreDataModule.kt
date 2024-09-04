package com.ag_apps.core.data.di

import com.ag_apps.core.data.networking.HttpClientFactory
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val coreDataModule = module {
    single {
        HttpClientFactory().build()
    }
}