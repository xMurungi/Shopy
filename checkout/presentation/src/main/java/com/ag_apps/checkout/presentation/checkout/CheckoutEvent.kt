package com.ag_apps.checkout.presentation.checkout

/**
 * @author Ahmed Guedmioui
 */
sealed interface CheckoutEvent {
    data class AddressSaved(val isSaved: Boolean): CheckoutEvent
    data class CardSaved(val isSaved: Boolean): CheckoutEvent
    data class OrderSubmitted(val isSubmitted: Boolean): CheckoutEvent
}