package com.ag_apps.profile.presentation

/**
 * @author Ahmed Guedmioui
 */
sealed interface ProfileEvent {
    data class AddressSaved(val isSaved: Boolean): ProfileEvent
    data object Logout: ProfileEvent
}