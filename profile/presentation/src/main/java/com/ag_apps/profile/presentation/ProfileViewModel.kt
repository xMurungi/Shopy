package com.ag_apps.profile.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.core.domain.UserDataSource
import com.ag_apps.core.domain.util.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
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
        observeAddressTextFieldStates()
        observeCardTextFieldStates()
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

            ProfileAction.OnCardToggle -> {
                state = state.copy(isEditeCardShowing = !state.isEditeCardShowing)
                if (state.isEditeCardShowing) {
                    setDefaultAddressAndCardInfo()
                }
            }

            ProfileAction.OnLogoutClick -> {
                viewModelScope.launch {
                    userDataSource.logout()
                    eventChannel.send(ProfileEvent.Logout)
                }
            }

            ProfileAction.OnSaveAddress -> {
                saveAddress()
            }

            ProfileAction.OnSaveCard -> {
                saveCard()
            }

        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            userDataSource.getUser().collect { userResult ->
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

        val card = state.user?.card
        card?.let {
            val cardNumber = if (card.cardNumber.isNotBlank()) {
                "...." + card.cardNumber.takeLast(4)
            } else {
                ""
            }
            state = state.copy(
                nameOnCardTextState = TextFieldState(""),
                cardNumberTextState = TextFieldState(cardNumber),
                expireDateTextState = TextFieldState(""),
                cvvTextState = TextFieldState(""),
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
            userDataSource.updateUser(state.user).collect { updateResult ->
                when (updateResult) {
                    is Result.Error -> {
                        state = state.copy(isSavingAddress = false)
                        eventChannel.send(ProfileEvent.AddressSave(false))
                    }

                    is Result.Success -> {
                        state = state.copy(
                            isSavingAddress = false,
                            isEditeAddressShowing = false
                        )
                        eventChannel.send(ProfileEvent.AddressSave(true))
                    }
                }
            }
        }
    }

    private fun saveCard() {
        state = state.copy(
            user = state.user?.copy(
                card = state.user?.card?.copy(
                    nameOnCard = state.nameOnCardTextState.text.toString(),
                    cardNumber = state.cardNumberTextState.text.toString(),
                    expireDate = state.expireDateTextState.text.toString(),
                    cvv = state.cvvTextState.text.toString()
                )
            )
        )

        viewModelScope.launch {
            state = state.copy(isSavingCard = true)
            userDataSource.updateUser(state.user).collect { updateResult ->
                when (updateResult) {
                    is Result.Error -> {
                        state = state.copy(isSavingCard = false)
                        eventChannel.send(ProfileEvent.CardSave(false))
                    }

                    is Result.Success -> {
                        state = state.copy(
                            isSavingCard = false,
                            isEditeCardShowing = false
                        )
                        eventChannel.send(ProfileEvent.CardSave(true))
                    }
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

    private fun observeCardTextFieldStates() {
        viewModelScope.launch {
            snapshotFlow { state.nameOnCardTextState.text }.collectLatest {
                setCanSaveCard()
            }
        }

        viewModelScope.launch {
            snapshotFlow { state.cardNumberTextState.text }.collectLatest {
                state = state.copy(isValidCardNumber = isValidCreditCard(it.toString()))
                setCanSaveCard()
            }
        }

        viewModelScope.launch {
            snapshotFlow { state.expireDateTextState.text }.collectLatest { input ->
                val filteredInput = input.filter { it.isDigit() }

                val formattedInput = when (filteredInput.length) {
                    in 0..2 -> filteredInput
                    in 3..4 -> "${filteredInput.substring(0, 2)}/${filteredInput.substring(2)}"
                    else -> "${filteredInput.substring(0, 2)}/${filteredInput.substring(2, 4)}"
                }

                state = state.copy(
                    expireDateTextState = TextFieldState(formattedInput.toString())
                )

                setCanSaveCard()
            }
        }

        viewModelScope.launch {
            snapshotFlow { state.cvvTextState.text }.collectLatest { input ->
                val sanitizedInput = input.filter { it.isDigit() }
                state = state.copy(
                    cvvTextState = TextFieldState(sanitizedInput.toString().take(3))
                )

                setCanSaveCard()
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

    private fun setCanSaveCard() {
        state = state.copy(
            canSavingCard = state.nameOnCardTextState.text.isNotBlank() &&
                    state.cardNumberTextState.text.isNotBlank() &&
                    state.expireDateTextState.text.isNotBlank() &&
                    state.cvvTextState.text.isNotBlank() &&
                    state.cvvTextState.text.length == 3 &&
                    isValidCreditCard(state.cardNumberTextState.text.toString())
        )
    }

    private fun isValidCreditCard(number: String): Boolean {
        val cardNumber = number.replace("[\\s-]".toRegex(), "")

        if (!cardNumber.all { it.isDigit() }) {
            return false
        }

        val reversedCardNumber = cardNumber.reversed()
        var sum = 0

        for ((index, digitChar) in reversedCardNumber.withIndex()) {
            val digit = digitChar.toString().toInt()

            sum += if (index % 2 == 1) {
                val doubledDigit = digit * 2
                if (doubledDigit > 9) doubledDigit - 9 else doubledDigit
            } else {
                digit
            }
        }

        return sum % 10 == 0
    }

}