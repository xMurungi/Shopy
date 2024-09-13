package com.ag_apps.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState

/**
 * @author Ahmed Guedmioui
 */
data class RegisterState(
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val canLogin: Boolean = false,
    val isLoggingIn: Boolean = false,
)















