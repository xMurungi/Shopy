package com.ag_apps.core.data.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.ag_apps.core.data.PrefsLocalStorageDataSource
import com.ag_apps.core.domain.abstractions.LocalStorageDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */
val coreDataModule = module {
    single {
        HttpClientFactory().build()
    }

    single<SharedPreferences> {
        EncryptedSharedPreferences(
            androidApplication(),
            "shopy_shared_prefs",
            MasterKey(androidApplication()),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    singleOf(::PrefsLocalStorageDataSource).bind<LocalStorageDataSource>()
}