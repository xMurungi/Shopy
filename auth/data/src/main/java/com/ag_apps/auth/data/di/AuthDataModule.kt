package com.ag_apps.auth.data.di

import com.ag_apps.auth.data.AuthRepositoryImpl
import com.ag_apps.auth.domain.AuthRepository
import com.ag_apps.auth.domain.PatternValidator
import com.ag_apps.auth.data.EmailPatternValidator
import com.ag_apps.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}




















