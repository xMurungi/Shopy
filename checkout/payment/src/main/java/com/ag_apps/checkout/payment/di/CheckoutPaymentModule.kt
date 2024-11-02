package com.ag_apps.checkout.payment.di

import com.ag_apps.checkout.payment.StripeClient
import org.koin.dsl.module

/**
 * @author Ahmed Guedmioui
 */

val checkoutPaymentModule = module {
    single { StripeClient(get(), get()) }
}