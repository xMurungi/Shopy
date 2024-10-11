package com.ag_apps.profile.presentation

/**
 * @author Ahmed Guedmioui
 */
sealed interface ProfileEvent {
    data class OnAddressSave(val isSaved: Boolean): ProfileEvent
    data class OnCardSave(val isSaved: Boolean): ProfileEvent
}