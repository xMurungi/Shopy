package com.ag_apps.search.data.di

import com.ag_apps.search.data.SearchRepositoryImpl
import com.ag_apps.search.domain.SearchRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val searchDataModule = module {
    singleOf(::SearchRepositoryImpl).bind<SearchRepository>()
}