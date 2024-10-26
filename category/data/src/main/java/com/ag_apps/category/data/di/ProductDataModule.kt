package com.ag_apps.category.data.di

import com.ag_apps.category.data.CategoryRepositoryImpl
import com.ag_apps.category.domain.CategoryRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val categoryDataModule = module {
    singleOf(::CategoryRepositoryImpl).bind<CategoryRepository>()
}