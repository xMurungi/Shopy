package com.ag_apps.core.product_data.di

import com.ag_apps.core.domain.abstractions.ProductDataSource
import com.ag_apps.core.product_data.LocalProductDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val coreProductDataModule = module {
    singleOf(::LocalProductDataSource).bind<ProductDataSource>()
}