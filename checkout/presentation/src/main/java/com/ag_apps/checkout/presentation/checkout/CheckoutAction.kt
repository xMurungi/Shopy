package com.ag_apps.checkout.presentation.checkout

/**
 * @author Ahmed Guedmioui
 */
sealed interface CheckoutAction {
    data object Refresh : CheckoutAction
    data object OnDisclaimerClick : CheckoutAction
    data object OnAddressToggle : CheckoutAction
    data object OnCardToggle : CheckoutAction
    data object OnSaveAddress : CheckoutAction
    data object OnSaveCard : CheckoutAction
    data object OnSubmitClick : CheckoutAction
    data object OnBackClick : CheckoutAction
}