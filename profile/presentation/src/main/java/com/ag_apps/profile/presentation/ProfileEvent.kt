package com.ag_apps.profile.presentation

/**
 * @author Ahmed Guedmioui
 */
sealed interface ProfileEvent {
    data class AddressSave(val isSaved: Boolean): ProfileEvent
    data class CardSave(val isSaved: Boolean): ProfileEvent
    data object Logout: ProfileEvent
}