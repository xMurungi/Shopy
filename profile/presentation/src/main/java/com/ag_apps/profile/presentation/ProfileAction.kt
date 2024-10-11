package com.ag_apps.profile.presentation

/**
 * @author Ahmed Guedmioui
 */
sealed interface ProfileAction {
    data object OnOrdersClick : ProfileAction
    data object OnAddressToggle : ProfileAction
    data object OnPaymentCardToggle : ProfileAction
    data object OnLogoutClick : ProfileAction

    sealed interface EditeAddressAction : ProfileAction {
        data class UpdateAddress(val newAddress: String) : EditeAddressAction
        data class UpdateCity(val newCity: String) : EditeAddressAction
        data class UpdateRegion(val newRegion: String) : EditeAddressAction
        data class UpdateZipCode(val newZipCode: String) : EditeAddressAction
        data class UpdateCountry(val newCountry: String) : EditeAddressAction
        data object OnSaveAddress : EditeAddressAction
    }

    sealed interface EditeCardAction : ProfileAction {
        data class UpdateNameOnCard(val newNameOnCard: String) : EditeCardAction
        data class UpdateCardNumber(val newCardNumber: String) : EditeCardAction
        data class UpdateExpireDate(val newExpireDate: String) : EditeCardAction
        data class UpdateCVV(val newCVV: String) : EditeCardAction
        data object OnSaveCard : EditeCardAction
    }
}