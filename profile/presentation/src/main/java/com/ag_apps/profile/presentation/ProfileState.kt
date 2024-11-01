package com.ag_apps.profile.presentation

import androidx.compose.foundation.text.input.TextFieldState
import com.ag_apps.core.domain.models.User

/**
 * @author Ahmed Guedmioui
 */
data class ProfileState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isEditeAddressShowing: Boolean = false,
    val isSavingAddress: Boolean = false,

    val canSavingAddress: Boolean = false,

    val streetTextState: TextFieldState = TextFieldState(""),
    val cityTextState: TextFieldState = TextFieldState(""),
    val regionTextState: TextFieldState = TextFieldState(""),
    val zipcodeTextState: TextFieldState = TextFieldState(""),
    val countryTextState: TextFieldState = TextFieldState(""),

    )
