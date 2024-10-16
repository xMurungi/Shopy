package com.ag_apps.profile.data.di

import com.ag_apps.profile.data.ProfileRepositoryImpl
import com.ag_apps.profile.domain.ProfileRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */

val profileDataModule = module {
    singleOf(::ProfileRepositoryImpl).bind<ProfileRepository>()
}




















