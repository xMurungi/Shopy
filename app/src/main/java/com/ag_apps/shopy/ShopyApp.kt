package com.ag_apps.shopy

import android.app.Application
import android.content.Context
import com.ag_apps.auth.data.di.authDataModule
import com.google.firebase.FirebaseApp
import com.ag_apps.auth.presentation.di.authPresentationModule
import com.ag_apps.cart.data.di.cartDataModule
import com.ag_apps.cart.presentation.di.cartPresentationModule
import com.ag_apps.category.data.di.categoryDataModule
import com.ag_apps.category.presentation.di.categoryPresentationModule
import com.ag_apps.checkout.data.di.checkoutDataModule
import com.ag_apps.checkout.presentation.di.checkoutPresentationModule
import com.ag_apps.core.user_data.di.coreUserDataModule
import com.ag_apps.core.product_data.di.coreProductDataModule
import com.ag_apps.product.data.di.productDataModule
import com.ag_apps.product.presentation.di.productPresentationModule
import com.ag_apps.profile.data.di.profileDataModule
import com.ag_apps.profile.presentation.di.profilePresentationModule
import com.ag_apps.search.data.di.searchDataModule
import com.ag_apps.search.presentation.di.searchPresentationModule
import com.ag_apps.shopy.di.appModule
import com.ag_apps.wishlist.data.di.wishlistDataModule
import com.ag_apps.wishlist.presentation.di.wishlistPresentationModule
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
                coreUserDataModule,

                authDataModule,
                authPresentationModule,

                profileDataModule,
                profilePresentationModule,

                productDataModule,
                productPresentationModule,

                searchDataModule,
                searchPresentationModule,

                categoryDataModule,
                categoryPresentationModule,

                wishlistDataModule,
                wishlistPresentationModule,

                cartDataModule,
                cartPresentationModule,

                checkoutDataModule,
                checkoutPresentationModule,
            )
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}





















