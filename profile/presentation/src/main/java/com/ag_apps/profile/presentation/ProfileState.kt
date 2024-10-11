package com.ag_apps.profile.presentation

import com.ag_apps.core.domain.User

/**
 * @author Ahmed Guedmioui
 */
data class ProfileState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isEditeAddressShowing: Boolean = false,
    val isSavingAddress: Boolean = false,
    val isEditeCardShowing: Boolean = false,
    val isSavingCard: Boolean = false
)
