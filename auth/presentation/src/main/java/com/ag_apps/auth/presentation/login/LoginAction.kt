package com.ag_apps.auth.presentation.login

/**
 * @author Ahmed Guedmioui
 */
sealed interface LoginAction {
    data object OnTogglePasswordVisibilityClick: LoginAction
    data object OnLoginClick: LoginAction
    data object OnGoogleLoginClick: LoginAction
    data object ObRegisterClick: LoginAction
}










