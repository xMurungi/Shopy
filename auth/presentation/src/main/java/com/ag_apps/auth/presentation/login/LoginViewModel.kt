package com.ag_apps.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.auth.domain.AuthRepository
import com.ag_apps.auth.domain.UserDataValidator
import com.ag_apps.auth.presentation.R
import com.ag_apps.core.domain.util.DataError
import com.ag_apps.core.domain.util.Result
import com.ag_apps.core.presentation.ui.UiText
import com.ag_apps.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userDataValidator: UserDataValidator
) : ViewModel() {

    private val TAG = "LoginViewModel"

    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            snapshotFlow { state.email.text }
                .collectLatest { email ->
                    println("$TAG email: $email")
                    state = state.copy(
                        canLogin = userDataValidator.isValidEmail(
                            email.toString().trim()
                        ) && state.password.text.isNotEmpty()
                    )
                }
        }

        viewModelScope.launch {
            snapshotFlow { state.password.text }
                .collectLatest { password ->
                    println("$TAG password: $password")
                    state = state.copy(
                        canLogin = userDataValidator.isValidEmail(
                            state.email.text.toString().trim()
                        ) && password.isNotEmpty()
                    )
                }
        }
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()

            LoginAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }

            LoginAction.OnGoogleLoginClick -> googleLogin()

            else -> Unit
        }
    }

    private fun googleLogin() {
        viewModelScope.launch {
            state = state.copy(isLoggingIn = true)

            when (val result = authRepository.googleLogin()) {
                is Result.Error -> {
                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        eventChannel.send(
                            LoginEvent.Error(
                                UiText.StringResource(R.string.error_google_login)
                            )
                        )
                    } else {
                        eventChannel.send(LoginEvent.Error(result.error.asUiText()))
                    }
                }

                is Result.Success -> {
                    eventChannel.send(LoginEvent.LoginSuccess)
                }
            }

            state = state.copy(isLoggingIn = false)
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLoggingIn = true)

            val result = authRepository.login(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString(),
            )

            when (result) {
                is Result.Error -> {
                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        eventChannel.send(
                            LoginEvent.Error(
                                UiText.StringResource(R.string.error_email_password_incorrect)
                            )
                        )
                    } else {
                        eventChannel.send(LoginEvent.Error(result.error.asUiText()))
                    }
                }

                is Result.Success -> {
                    eventChannel.send(LoginEvent.LoginSuccess)
                }
            }

            state = state.copy(isLoggingIn = false)
        }
    }

}

















