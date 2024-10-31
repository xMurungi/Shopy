package com.ag_apps.profile.presentation

/**
 * @author Ahmed Guedmioui
 */
sealed interface ProfileAction {
    data object OnOrdersClick : ProfileAction
    data object OnAddressToggle : ProfileAction
    data object OnLogoutClick : ProfileAction

    data object OnSaveAddress : ProfileAction
}