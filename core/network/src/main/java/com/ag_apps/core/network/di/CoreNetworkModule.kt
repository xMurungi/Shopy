package com.ag_apps.core.network.di

import com.ag_apps.core.network.networking.HttpClientFactory
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val coreNetworkModule = module {
    single {
        HttpClientFactory().build()
    }
}