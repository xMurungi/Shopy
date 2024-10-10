package com.ag_apps.core.network.di

import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.network.FirebaseUserDataSource
import com.ag_apps.core.network.FirestoreClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */

val coreNetworkModule = module {

    single { FirestoreClient(get()) }

    singleOf(::FirebaseUserDataSource).bind<UserDataSource>()
}