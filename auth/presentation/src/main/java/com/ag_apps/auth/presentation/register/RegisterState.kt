package com.ag_apps.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import com.ag_apps.auth.domain.PasswordValidationState


/**
 * @author Ahmed Guedmioui
 */
data class RegisterState(
    val email: TextFieldState = TextFieldState(),
    val name: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val isEmailValid: Boolean = false,
    val isNameValid: Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val isRegistering: Boolean = false,
    val canRegister: Boolean = false,
    val isPasswordVisible: Boolean = false
)















