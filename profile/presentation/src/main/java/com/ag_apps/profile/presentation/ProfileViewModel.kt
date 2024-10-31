package com.ag_apps.profile.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.core.domain.util.Result
import com.ag_apps.profile.domain.ProfileRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    private var eventChannel = Channel<ProfileEvent>()
    val event = eventChannel.receiveAsFlow()

    init {
        loadUser()
        observeAddressTextFieldStates()
    }

    fun onAction(action: ProfileAction) {
        when (action) {

            ProfileAction.OnOrdersClick -> Unit

            ProfileAction.OnAddressToggle -> {
                state = state.copy(isEditeAddressShowing = !state.isEditeAddressShowing)
                if (state.isEditeAddressShowing) {
                    setDefaultAddressAndCardInfo()
                }
            }

            ProfileAction.OnLogoutClick -> {
                viewModelScope.launch {
                    profileRepository.logout()
                    eventChannel.send(ProfileEvent.Logout)
                }
            }

            ProfileAction.OnSaveAddress -> {
                saveAddress()
            }

        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val userResult = profileRepository.getUser()
            when (userResult) {
                is Result.Error -> {
                    state = state.copy(isLoading = false)
                }

                is Result.Success -> {
                    state = state.copy(
                        isLoading = false,
                        user = userResult.data
                    )

                    setDefaultAddressAndCardInfo()
                }
            }
        }
    }

    private fun setDefaultAddressAndCardInfo() {
        val address = state.user?.address
        address?.let {
            state = state.copy(
                streetTextState = TextFieldState(address.street),
                cityTextState = TextFieldState(address.city),
                zipcodeTextState = TextFieldState(address.zipCode),
                regionTextState = TextFieldState(address.region),
                countryTextState = TextFieldState(address.country)
            )
        }
    }

    private fun saveAddress() {
        state = state.copy(
            user = state.user?.copy(
                address = state.user?.address?.copy(
                    street = state.streetTextState.text.toString(),
                    city = state.cityTextState.text.toString(),
                    region = state.regionTextState.text.toString(),
                    zipCode = state.zipcodeTextState.text.toString(),
                    country = state.countryTextState.text.toString()
                )
            )
        )

        viewModelScope.launch {
            state = state.copy(isSavingAddress = true)
            val updateResult = profileRepository.updateUser(state.user)
            when (updateResult) {
                is Result.Error -> {
                    state = state.copy(isSavingAddress = false)
                    eventChannel.send(ProfileEvent.AddressSaved(false))
                }

                is Result.Success -> {
                    state = state.copy(
                        isSavingAddress = false,
                        isEditeAddressShowing = false
                    )
                    eventChannel.send(ProfileEvent.AddressSaved(true))
                }
            }
        }
    }


    private fun observeAddressTextFieldStates() {
        viewModelScope.launch {
            snapshotFlow { state.streetTextState.text }.collectLatest {
                setCanSaveAddress()
            }
        }

        viewModelScope.launch {
            snapshotFlow { state.cityTextState.text }.collectLatest {
                setCanSaveAddress()
            }
        }

        viewModelScope.launch {
            snapshotFlow { state.regionTextState.text }.collectLatest {
                setCanSaveAddress()
            }
        }

        viewModelScope.launch {
            snapshotFlow { state.zipcodeTextState.text }.collectLatest {
                setCanSaveAddress()
            }
        }

        viewModelScope.launch {
            snapshotFlow { state.countryTextState.text }.collectLatest {
                setCanSaveAddress()
            }
        }

    }


    private fun setCanSaveAddress() {
        state = state.copy(
            canSavingAddress = state.streetTextState.text.isNotBlank() &&
                    state.cityTextState.text.isNotBlank() &&
                    state.regionTextState.text.isNotBlank() &&
                    state.zipcodeTextState.text.isNotBlank() &&
                    state.countryTextState.text.isNotBlank()
        )
    }

}