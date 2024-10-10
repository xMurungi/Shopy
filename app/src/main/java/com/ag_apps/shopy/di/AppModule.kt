package com.ag_apps.shopy.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.ag_apps.shopy.MainViewModel
import com.ag_apps.shopy.ShopyApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as ShopyApp).applicationScope
    }

    viewModel { MainViewModel(get()) }
}




















