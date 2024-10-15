package com.ag_apps.core.user_data.di

import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.user_data.FirebaseUserDataSource
import com.ag_apps.core.user_data.FirestoreClient
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