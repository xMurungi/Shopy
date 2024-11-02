package com.ag_apps.checkout.presentation.checkout

import com.ag_apps.core.domain.util.Error
import com.ag_apps.core.domain.util.Result

/**
 * @author Ahmed Guedmioui
 */
sealed interface CheckoutAction {
    data object OnRefresh : CheckoutAction
    data object OnDisclaimerClick : CheckoutAction
    data object OnAddressToggle : CheckoutAction
    data object OnCardToggle : CheckoutAction
    data object OnSaveAddress : CheckoutAction
    data object OnSaveCard : CheckoutAction
    data object OnBackClick : CheckoutAction
    data object OnCheckoutClick : CheckoutAction
    data class OnSubmitResult(val result: Result<Unit, Error>) : CheckoutAction
}