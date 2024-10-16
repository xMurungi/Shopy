package com.ag_apps.profile.presentation

import androidx.compose.foundation.text.input.TextFieldState
import com.ag_apps.core.domain.Card
import com.ag_apps.core.domain.User

/**
 * @author Ahmed Guedmioui
 */
data class ProfileState(
    val user: User? = null,
    val card: Card? = null,
    val isLoading: Boolean = false,
    val isEditeAddressShowing: Boolean = false,
    val isSavingAddress: Boolean = false,
    val isEditeCardShowing: Boolean = false,
    val isSavingCard: Boolean = false,

    val canSavingAddress: Boolean = false,
    val canSavingCard: Boolean = false,

    val streetTextState: TextFieldState = TextFieldState(""),
    val cityTextState: TextFieldState = TextFieldState(""),
    val regionTextState: TextFieldState = TextFieldState(""),
    val zipcodeTextState: TextFieldState = TextFieldState(""),
    val countryTextState: TextFieldState = TextFieldState(""),

    val nameOnCardTextState: TextFieldState = TextFieldState(""),
    val cardNumberTextState: TextFieldState = TextFieldState(""),
    val expireDateTextState: TextFieldState = TextFieldState(""),
    val cvvTextState: TextFieldState = TextFieldState(""),

    val isValidNameOnCard: Boolean = false,
    val isValidCardNumber: Boolean = false,
    val isValidExpireDate: Boolean = false,
    val isValidCVV: Boolean = false,
)
