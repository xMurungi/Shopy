package com.ag_apps.checkout.presentation.checkout

import com.ag_apps.core.presentation.ui.UiText

/**
 * @author Ahmed Guedmioui
 */
sealed interface CheckoutEvent {
    data class AddressSaved(val isSaved: Boolean) : CheckoutEvent
    data class CardSaved(val isSaved: Boolean) : CheckoutEvent
    data class OrderSubmitted(
        val isSubmitted: Boolean, val error: UiText? = null
    ) : CheckoutEvent
}