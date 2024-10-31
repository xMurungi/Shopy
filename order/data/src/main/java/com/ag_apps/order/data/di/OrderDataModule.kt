package com.ag_apps.order.data.di

import com.ag_apps.order.data.OrderRepositoryImpl
import com.ag_apps.order.domain.OrderRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val orderDataModule = module {
    singleOf(::OrderRepositoryImpl).bind<OrderRepository>()
}