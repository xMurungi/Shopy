package com.ag_apps.product.presentation

/**
 * @author Ahmed Guedmioui
 */
sealed interface ProductEvent {
    data class AddressSave(val isSaved: Boolean): ProductEvent
    data class CardSave(val isSaved: Boolean): ProductEvent
    data object Logout: ProductEvent
}