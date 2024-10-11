package com.ag_apps.auth.presentation.register

import RegisterEvent
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.ag_apps.core.domain.util.Result
import com.ag_apps.core.presentation.ui.UiText
import com.ag_apps.core.presentation.ui.asUiText

/**
 * @author Ahmed Guedmioui
 */
class RegisterViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    private val eventChannel = Channel<RegisterEvent>()
    val event = eventChannel.receiveAsFlow()

    init {

        state = state.copy(
            email = TextFieldState("ahmed@gmail.com"),
            name = TextFieldState("Ahmed"),
            password = TextFieldState("Ahmed12345")
        )

        viewModelScope.launch {
            snapshotFlow { state.email.text }
                .collectLatest { email ->
                    val isValidEmail = userDataValidator.isValidEmail(email.toString())

                    val canRegister = isValidEmail
                            && state.isNameValid
                            && state.passwordValidationState.isValidPassword
                            && !state.isRegistering

                    state = state.copy(
                        isEmailValid = isValidEmail,
                        canRegister = canRegister
                    )
                }
        }

        viewModelScope.launch {
            snapshotFlow { state.name.text }
                .collectLatest { name ->
                    val isValidName = userDataValidator.isValidName(name.toString())

                    val canRegister = isValidName
                            && state.isEmailValid
                            && state.passwordValidationState.isValidPassword
                            && !state.isRegistering

                    state = state.copy(
                        isNameValid = isValidName,
                        canRegister = canRegister
                    )
                }
        }

        viewModelScope.launch {
            snapshotFlow { state.password.text }
                .collectLatest { password ->
                    val passwordValidationState = userDataValidator.isValidPassword(password.toString())

                    val canRegister = passwordValidationState.isValidPassword
                            && state.isEmailValid
                            && state.isNameValid
                            && !state.isRegistering

                    state = state.copy(
                        passwordValidationState = passwordValidationState,
                        canRegister = canRegister
                    )
                }
        }
    }

    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.ObRegisterClick -> register()

            RegisterAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }

            RegisterAction.OnGoogleRegisterClick -> googleRegister()

            RegisterAction.OnLoginClick -> Unit
        }
    }

    private fun googleRegister() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true, canRegister = false)

            authRepository.googleSignIn().collect { result ->
                when (result) {
                    is Result.Error -> {
                        state = state.copy(isRegistering = false, canRegister = true)
                        when (result.error) {
                            DataError.Network.UNAUTHORIZED -> {
                                eventChannel.send(
                                    RegisterEvent.Error(
                                        UiText.StringResource(R.string.error_google_login)
                                    )
                                )
                            }

                            DataError.Network.UNKNOWN -> {
                                eventChannel.send(
                                    RegisterEvent.Error(
                                        UiText.StringResource(R.string.error_unknown_try_again)
                                    )
                                )
                            }

                            else -> {
                                eventChannel.send(RegisterEvent.Error(result.error.asUiText()))
                            }
                        }
                    }

                    is Result.Success -> {
                        state = state.copy(isRegistering = false, canRegister = true)
                        eventChannel.send(RegisterEvent.RegistrationSuccess)
                    }
                }
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true)

            authRepository.register(
                email = state.email.text.toString().trim(),
                name = state.name.text.toString(),
                password = state.password.text.toString()
            ).collect { result ->
                when (result) {
                    is Result.Error -> {
                        state = state.copy(isRegistering = false)
                        if (result.error == DataError.Network.CONFLICT) {
                            eventChannel.send(
                                RegisterEvent.Error(UiText.StringResource(R.string.error_email_exists))
                            )
                        } else {
                            eventChannel.send(RegisterEvent.Error(result.error.asUiText()))
                        }
                    }

                    is Result.Success -> {
                        state = state.copy(isRegistering = false)
                        eventChannel.send(RegisterEvent.RegistrationSuccess)
                    }
                }
            }
        }
    }
}




















