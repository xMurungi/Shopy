package com.ag_apps.shopy

import android.app.Application
import android.content.Context
import com.ag_apps.auth.data.di.authDataModule
import com.ag_apps.auth.presentation.di.authPresentationModule
import com.ag_apps.core.data.di.coreDataModule
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

        startKoin {
            androidLogger()
            androidContext(this@ShopyApp)
            modules(
                appModule,
                coreDataModule,
                authDataModule,
                authPresentationModule
            )
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}




















