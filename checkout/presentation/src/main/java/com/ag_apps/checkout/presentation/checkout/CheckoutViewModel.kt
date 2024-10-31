package com.ag_apps.checkout.presentation.checkout

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.checkout.domain.CheckoutRepository
import com.ag_apps.core.domain.Card
import com.ag_apps.core.domain.util.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class CheckoutViewModel(
    private val checkoutRepository: CheckoutRepository
) : ViewModel() {

    var state by mutableStateOf(CheckoutState())
        private set

    private var eventChannel = Channel<CheckoutEvent>()
    val event = eventChannel.receiveAsFlow()

    init {
        state = state.copy(totalPrice = null)
        observeAddressTextFieldStates()
        observeCardTextFieldStates()
    }

    fun onAction(action: CheckoutAction) {
        when (action) {

            CheckoutAction.Refresh -> {
                loadUser()
            }

            CheckoutAction.OnDisclaimerClick -> Unit

            CheckoutAction.OnAddressToggle -> {
                state = state.copy(isEditeAddressShowing = !state.isEditeAddressShowing)
                if (state.isEditeAddressShowing) {
                    setDefaultAddressAndCardInfo()
                }
            }

            CheckoutAction.OnCardToggle -> {
                state = state.copy(isEditeCardShowing = !state.isEditeCardShowing)
                if (state.isEditeCardShowing) {
                    setDefaultAddressAndCardInfo()
                }
            }


            CheckoutAction.OnSaveAddress -> {
                saveAddress()
            }

            CheckoutAction.OnSaveCard -> {
                saveCard()
            }

            CheckoutAction.OnSubmitClick -> {
                viewModelScope.launch {
                    eventChannel.send(CheckoutEvent.OrderSubmitted(true))
                    checkoutRepository.submitOrder(state.user, state.totalPrice)
                }
            }

            CheckoutAction.OnBackClick -> Unit
        }
    }

    private fun loadUser() {
        state = state.copy(
            isLoading = true, isError = false
        )

        viewModelScope.launch {


            when (val userResult = checkoutRepository.getUser()) {
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        isError = true
                    )
                }

                is Result.Success -> {
                    loadTotalPrice()
                    state = state.copy(
                        isLoading = false,
                        isError = false,
                        user = userResult.data
                    )

                    setDefaultAddressAndCardInfo()
                }
            }
        }
    }

    private suspend fun loadTotalPrice() {

        state = state.copy(totalPrice = null)

        state = when (val totalPriceResult = checkoutRepository.getTotalPrice()) {
            is Result.Error -> {
                state.copy(
                    totalPrice = null,
                    isError = true
                )
            }

            is Result.Success -> {
                state.copy(
                    totalPrice = totalPriceResult.data
                )
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

        val card = state.card
        card?.let {
            state = state.copy(
                nameOnCardTextState = TextFieldState(card.nameOnCard),
                cardNumberTextState = TextFieldState(card.cardNumber),
                expireDateTextState = TextFieldState(card.expireDate),
                cvvTextState = TextFieldState(card.cvv)
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
            val updateResult = checkoutRepository.updateUser(state.user)
            when (updateResult) {
                is Result.Error -> {
                    state = state.copy(isSavingAddress = false)
                    eventChannel.send(CheckoutEvent.AddressSaved(false))
                }

                is Result.Success -> {
                    state = state.copy(
                        isSavingAddress = false,
                        isEditeAddressShowing = false
                    )
                    eventChannel.send(CheckoutEvent.AddressSaved(true))
                }
            }
        }
    }

    private fun saveCard() {
        state = state.copy(
            card = Card(
                nameOnCard = state.nameOnCardTextState.text.toString(),
                cardNumber = state.cardNumberTextState.text.toString(),
                expireDate = state.expireDateTextState.text.toString(),
                cvv = state.cvvTextState.text.toString()
            )
        )
        state = state.copy(
            isEditeCardShowing = false,
        )
        viewModelScope.launch {
            eventChannel.send(CheckoutEvent.CardSaved(true))
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
                state = state.copy(isValidCardNumber = isValidCardNumber(it.toString()))
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
                    isValidCardNumber(state.cardNumberTextState.text.toString())
        )
    }

    private fun isValidCardNumber(cardNumber: String): Boolean {
        // Remove any spaces or hyphens from the input
        val sanitizedNumber = cardNumber.replace(" ", "").replace("-", "")

        // Check if the card number length is valid (between 13 and 19 digits)
        if (sanitizedNumber.length !in 13..19 || !sanitizedNumber.all { it.isDigit() }) {
            return false
        }

        // Perform Luhn algorithm for checksum validation
        return luhnCheck(sanitizedNumber)
    }

    private fun luhnCheck(cardNumber: String): Boolean {
        var sum = 0
        var alternate = false

        // Iterate over the card number from right to left
        for (i in cardNumber.length - 1 downTo 0) {
            var n = cardNumber[i].digitToInt()

            if (alternate) {
                n *= 2
                if (n > 9) n -= 9
            }

            sum += n
            alternate = !alternate
        }

        // The card number is valid if the total sum is divisible by 10
        return sum % 10 == 0
    }

    fun getCardBrand(cardNumber: String): String? {
        // Remove spaces or hyphens from input
        val sanitizedNumber = cardNumber.replace(" ", "").replace("-", "")

        // Check if the input is purely numeric
        if (!sanitizedNumber.all { it.isDigit() }) return null

        return when {
            sanitizedNumber.startsWith("4") && sanitizedNumber.length in 13..16 -> "Visa"
            sanitizedNumber.startsWith("5") && sanitizedNumber.substring(0, 2).toInt() in 51..55 -> "MasterCard"
            sanitizedNumber.startsWith("22") && sanitizedNumber.substring(0, 4).toInt() in 2221..2720 -> "MasterCard"
            sanitizedNumber.startsWith("34") || sanitizedNumber.startsWith("37") && sanitizedNumber.length == 15 -> "American Express"
            sanitizedNumber.startsWith("6011") || sanitizedNumber.substring(0, 3).toInt() in 644..649 || sanitizedNumber.startsWith("65") -> "Discover"
            sanitizedNumber.substring(0, 4).toInt() in 3528..3589 -> "JCB"
            sanitizedNumber.substring(0, 3).toInt() in 300..305 || sanitizedNumber.startsWith("36") || sanitizedNumber.startsWith("38") -> "Diners Club"
            sanitizedNumber.startsWith("50") || sanitizedNumber.substring(0, 2).toInt() in 56..69 -> "Maestro"
            else -> "Unknown"
        }
    }


}