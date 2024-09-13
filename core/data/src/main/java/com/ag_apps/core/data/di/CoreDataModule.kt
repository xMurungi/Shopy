package com.ag_apps.core.data.di

import com.ag_apps.core.data.auth.EncryptedSessionStorage
import com.ag_apps.core.domain.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val coreDataModule = module {

    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()

}