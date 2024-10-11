package com.ag_apps.profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.domain.util.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class ProfileViewModel(
    private val userDataSource: UserDataSource
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    private var eventChannel = Channel<ProfileEvent>()
    val event = eventChannel.receiveAsFlow()

    init {
        loadUser()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnAddressToggle -> {
                state = state.copy(isEditeAddressShowing = !state.isEditeAddressShowing)
            }

            ProfileAction.OnPaymentCardToggle -> {
                state = state.copy(isEditeCardShowing = !state.isEditeCardShowing)
            }

            ProfileAction.OnLogoutClick -> {
                userDataSource.logout()
            }

            ProfileAction.OnOrdersClick -> Unit

            is ProfileAction.EditeAddressAction.UpdateAddress -> onEditeAddressAction(action)
            is ProfileAction.EditeAddressAction.UpdateCity -> onEditeAddressAction(action)
            is ProfileAction.EditeAddressAction.UpdateCountry -> onEditeAddressAction(action)
            is ProfileAction.EditeAddressAction.UpdateRegion -> onEditeAddressAction(action)
            is ProfileAction.EditeAddressAction.UpdateZipCode -> onEditeAddressAction(action)
            is ProfileAction.EditeAddressAction.OnSaveAddress -> onEditeAddressAction(action)

            is ProfileAction.EditeCardAction.UpdateNameOnCard -> onEditeCardAction(action)
            is ProfileAction.EditeCardAction.UpdateCardNumber -> onEditeCardAction(action)
            is ProfileAction.EditeCardAction.UpdateExpireDate -> onEditeCardAction(action)
            is ProfileAction.EditeCardAction.UpdateCVV -> onEditeCardAction(action)
            is ProfileAction.EditeCardAction.OnSaveCard -> onEditeCardAction(action)

        }
    }

    private fun onEditeAddressAction(action: ProfileAction.EditeAddressAction) {
        when (action) {
            is ProfileAction.EditeAddressAction.UpdateAddress -> {
                state = state.copy(
                    user = state.user?.copy(
                        address = state.user?.address?.copy(address = action.newAddress)
                    )
                )
            }

            is ProfileAction.EditeAddressAction.UpdateCity -> {
                state = state.copy(
                    user = state.user?.copy(
                        address = state.user?.address?.copy(address = action.newCity)
                    )
                )
            }

            is ProfileAction.EditeAddressAction.UpdateRegion -> {
                state = state.copy(
                    user = state.user?.copy(
                        address = state.user?.address?.copy(address = action.newRegion)
                    )
                )
            }

            is ProfileAction.EditeAddressAction.UpdateZipCode -> {
                state = state.copy(
                    user = state.user?.copy(
                        address = state.user?.address?.copy(address = action.newZipCode)
                    )
                )
            }

            is ProfileAction.EditeAddressAction.UpdateCountry -> {
                state = state.copy(
                    user = state.user?.copy(
                        address = state.user?.address?.copy(address = action.newCountry)
                    )
                )
            }

            ProfileAction.EditeAddressAction.OnSaveAddress -> {
                saveAddress()
            }
        }
    }

    private fun onEditeCardAction(action: ProfileAction.EditeCardAction) {
        when (action) {
            is ProfileAction.EditeCardAction.UpdateNameOnCard -> {
                state = state.copy(
                    user = state.user?.copy(
                        card = state.user?.card?.copy(cvv = action.newNameOnCard)
                    )
                )
            }

            is ProfileAction.EditeCardAction.UpdateCardNumber -> {
                state = state.copy(
                    user = state.user?.copy(
                        card = state.user?.card?.copy(cvv = action.newCardNumber)
                    )
                )
            }

            is ProfileAction.EditeCardAction.UpdateExpireDate -> {
                state = state.copy(
                    user = state.user?.copy(
                        card = state.user?.card?.copy(cvv = action.newExpireDate)
                    )
                )
            }

            is ProfileAction.EditeCardAction.UpdateCVV -> {
                state = state.copy(
                    user = state.user?.copy(
                        card = state.user?.card?.copy(cvv = action.newCVV)
                    )
                )
            }

            ProfileAction.EditeCardAction.OnSaveCard -> {
                saveCard()
            }
        }
    }

    private fun saveAddress() {
        viewModelScope.launch {
            state = state.copy(isSavingAddress = true)
            userDataSource.updateUser(state.user).collect { updateResult ->
                when (updateResult) {
                    is Result.Error -> {
                        state = state.copy(isSavingAddress = false)
                        eventChannel.send(ProfileEvent.OnAddressSave(false))
                    }

                    is Result.Success -> {
                        state = state.copy(
                            isSavingAddress = false,
                            isEditeAddressShowing = false
                        )
                        eventChannel.send(ProfileEvent.OnAddressSave(true))
                    }
                }
            }
        }
    }

    private fun saveCard() {
        viewModelScope.launch {
            state = state.copy(isSavingCard = true)
            userDataSource.updateUser(state.user).collect { updateResult ->
                when (updateResult) {
                    is Result.Error -> {
                        state = state.copy(isSavingCard = false)
                        eventChannel.send(ProfileEvent.OnCardSave(false))
                    }

                    is Result.Success -> {
                        state = state.copy(
                            isSavingCard = false,
                            isEditeCardShowing = false
                        )
                        eventChannel.send(ProfileEvent.OnCardSave(true))
                    }
                }
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            userDataSource.getUser().collect { userResult ->
                state = when (userResult) {
                    is Result.Error -> {
                        state.copy(isLoading = false)
                    }

                    is Result.Success -> {
                        state.copy(
                            isLoading = false,
                            user = userResult.data
                        )
                    }
                }
            }
        }
    }

}