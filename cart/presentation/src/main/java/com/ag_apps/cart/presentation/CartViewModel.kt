package com.ag_apps.cart.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.cart.domain.CartRepository
import com.ag_apps.core.domain.util.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    var state by mutableStateOf(CartState())
        private set

    private val eventChannel = Channel<CartEvent>()
    val event = eventChannel.receiveAsFlow()

    fun onAction(action: CartAction) {
        when (action) {
            CartAction.Refresh -> {
                loadCartProducts()
            }

            is CartAction.ToggleProductInWishlist -> {
                toggleProductInWishlist(action.productIndex)
            }

            is CartAction.ClickProduct -> Unit

            CartAction.Checkout -> Unit
        }
    }

    private fun loadCartProducts() {
        println("CartViewModel.loadCartProducts")
        viewModelScope.launch {

            state = state.copy(
                isLoading = true,
                isError = false,
            )


            when (val productsResult = cartRepository.getCartProducts()) {

                is Result.Success -> {
                    state = state.copy(
                        isLoading = false,
                        isError = false,
                        products = productsResult.data,
                        totalPrice = productsResult.data.sumOf { it.price }
                    )
                }

                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        isError = true
                    )
                }
            }
        }
    }

    private fun toggleProductInWishlist(index: Int) {
        viewModelScope.launch {

            state = state.copy(
                products = state.products.map { product ->
                    if (product.productId == state.products[index].productId) {
                        product.copy(isInWishList = !state.products[index].isInWishList)
                    } else {
                        product
                    }
                },
                totalPrice = state.products.sumOf { it.price }
            )

            if (state.products[index].isInWishList) {
                cartRepository.addProductToWishlist(
                    productId = state.products[index].productId,
                )
            } else {
                cartRepository.removeProductFromWishlist(state.products[index].productId)
            }
        }
    }

}