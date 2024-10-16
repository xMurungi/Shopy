package com.ag_apps.shopy

import android.app.Application
import android.content.Context
import com.ag_apps.auth.data.di.authDataModule
import com.google.firebase.FirebaseApp
import com.ag_apps.auth.presentation.di.authPresentationModule
import com.ag_apps.core.user_data.di.coreNetworkModule
import com.ag_apps.core.product_data.di.coreProductDataModule
import com.ag_apps.product.data.di.productDataModule
import com.ag_apps.product.presentation.di.productPresentationModule
import com.ag_apps.profile.data.di.profileDataModule
import com.ag_apps.profile.presentation.di.profilePresentationModule
import com.ag_apps.shopy.di.appModule
import com.google.android.play.core.splitcompat.SplitCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * @author Ahmed Guedmioui
 */
class ShopyApp: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        FirebaseApp.initializeApp(this)

        startKoin {
            androidLogger()
            androidContext(this@ShopyApp)
            modules(
                appModule,
                coreProductDataModule,
                coreNetworkModule,
                authDataModule,
                authPresentationModule,
                profileDataModule,
                profilePresentationModule,
                productDataModule,
                productPresentationModule
            )
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}





















