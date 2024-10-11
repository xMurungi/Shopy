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

    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()
    val event = eventChannel.receiveAsFlow()

    init {
        state = state.copy(
            email = TextFieldState("ahmed@gmail.com"),
            password = TextFieldState("Ahmed12345")
        )

        viewModelScope.launch {
            snapshotFlow { state.email.text }
                .collectLatest { email ->
                    state = state.copy(
                        canLogin = userDataValidator.isValidEmail(
                            email.toString().trim()
                        ) && state.password.text.isNotEmpty() && !state.isLoggingIn
                    )
                }
        }

        viewModelScope.launch {
            snapshotFlow { state.password.text }
                .collectLatest { password ->
                    state = state.copy(
                        canLogin = userDataValidator.isValidEmail(
                            state.email.text.toString().trim()
                        ) && password.isNotEmpty() && !state.isLoggingIn
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

            LoginAction.ObRegisterClick -> Unit
        }
    }

    private fun googleLogin() {
        viewModelScope.launch {
            state = state.copy(isLoggingIn = true, canLogin = false)
            authRepository.googleSignIn().collect { result ->
                when (result) {
                    is Result.Error -> {
                        state = state.copy(isLoggingIn = false, canLogin = true)
                        when (result.error) {
                            DataError.Network.UNAUTHORIZED -> {
                                eventChannel.send(
                                    LoginEvent.Error(
                                        UiText.StringResource(R.string.error_google_login)
                                    )
                                )
                            }

                            DataError.Network.UNKNOWN -> {
                                eventChannel.send(
                                    LoginEvent.Error(
                                        UiText.StringResource(R.string.error_unknown_try_again)
                                    )
                                )
                            }

                            else -> {
                                eventChannel.send(LoginEvent.Error(result.error.asUiText()))
                            }
                        }
                    }

                    is Result.Success -> {
                        state = state.copy(isLoggingIn = false, canLogin = true)
                        eventChannel.send(LoginEvent.LoginSuccess)
                    }
                }
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLoggingIn = true)

            authRepository.login(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString(),
            ).collect { result ->
                when (result) {
                    is Result.Error -> {
                        state = state.copy(isLoggingIn = false)
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
                        state = state.copy(isLoggingIn = false)
                        eventChannel.send(LoginEvent.LoginSuccess)
                    }
                }
            }
        }
    }

}

















