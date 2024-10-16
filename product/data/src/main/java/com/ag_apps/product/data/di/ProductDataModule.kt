package com.ag_apps.product.data.di

import com.ag_apps.product.data.ProductRepositoryImpl
import com.ag_apps.product.domain.ProductRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val productDataModule = module {
    singleOf(::ProductRepositoryImpl).bind<ProductRepository>()
}