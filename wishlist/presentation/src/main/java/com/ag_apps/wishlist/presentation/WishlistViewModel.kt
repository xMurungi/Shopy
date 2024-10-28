package com.ag_apps.wishlist.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.core.domain.util.Result
import com.ag_apps.wishlist.domain.WishlistRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class WishlistViewModel(
    private val wishlistRepository: WishlistRepository
) : ViewModel() {

    var state by mutableStateOf(WishlistState())
        private set

    private val eventChannel = Channel<WishlistEvent>()
    val event = eventChannel.receiveAsFlow()

    fun onAction(action: WishlistAction) {
        when (action) {
            WishlistAction.Refresh -> {
                loadWishlistProducts()
            }

            is WishlistAction.RemoveProductFromWishlist -> {
                removeProductFromWishlist(action.productIndex)
            }

            is WishlistAction.ToggleProductInCart -> {
                toggleProductInCart(action.productIndex)
            }

            is WishlistAction.ClickProduct -> Unit

            WishlistAction.Search -> Unit
        }
    }

    private fun loadWishlistProducts() {
        println("WishlistViewModel.loadWishlistProducts")
        viewModelScope.launch {

            state = state.copy(
                isLoading = true,
                isError = false,
            )


            when (val productsResult = wishlistRepository.getWishlistProducts()) {

                is Result.Success -> {
                    state = state.copy(
                        isLoading = false,
                        isError = false,
                        products = productsResult.data
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

    private fun removeProductFromWishlist(index: Int) {
        viewModelScope.launch {
            wishlistRepository.removeProductFromWishlist(state.products[index].productId)
            state = state.copy(
                products = state.products.filter { product ->
                    product.productId != state.products[index].productId
                }
            )
        }
    }

    private fun toggleProductInCart(index: Int) {
        viewModelScope.launch {

            state = state.copy(
                products = state.products.map { product ->
                    if (product.productId == state.products[index].productId) {
                        product.copy(isInCartList = !state.products[index].isInCartList)
                    } else {
                        product
                    }
                }
            )

            if (state.products[index].isInCartList) {
                wishlistRepository.addProductToCart(
                    productId = state.products[index].productId,
                    filter = state.products[index].filterList.firstOrNull()
                )
            } else {
                wishlistRepository.removeProductFromCart(state.products[index].productId)
            }
        }
    }

}