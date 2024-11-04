package com.ag_apps.order.presentation.order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.core.domain.util.Result
import com.ag_apps.order.domain.OrderRepository
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class OrderViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    var state by mutableStateOf(OrderState())
        private set


    fun onAction(action: OrderAction) {
        when (action) {
            is OrderAction.LoadOrder -> {
                state = state.copy(orderId = action.orderId)
                loadOrder(action.orderId)
            }

            is OrderAction.OnProductClick -> Unit

            is OrderAction.ToggleProductInWishlist -> {
                toggleProductInWishlist(action.productIndex)
            }

            is OrderAction.ToggleProductInCart -> {
                toggleProductInCart(action.productIndex)
            }

            OrderAction.OnBackClick -> Unit

            OrderAction.Refresh -> {
                state.orderId?.let { loadOrder(it) }
            }
        }
    }

    private fun loadOrder(orderId: Int) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            when (val orderResult = orderRepository.getOrder(orderId)) {
                is Result.Error -> {
                    state = state.copy(
                        isError = true,
                        isLoading = false
                    )
                }

                is Result.Success -> {
                    state = state.copy(
                        isError = false,
                        order = orderResult.data
                    )
                    loadOrderProducts(orderResult.data.products)
                }
            }
        }
    }

    private suspend fun loadOrderProducts(productsMap: Map<Int, String?>) {
        state = when (val productsResult = orderRepository.getOrderProducts(productsMap)) {
            is Result.Error -> {
                state.copy(
                    isLoading = false
                )
            }

            is Result.Success -> {
                state.copy(
                    isLoading = false,
                    products = productsResult.data
                )
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
                }
            )

            if (state.products[index].isInWishList) {
                orderRepository.addProductToWishlist(state.products[index].productId)
            } else {
                orderRepository.removeProductFromWishlist(state.products[index].productId)
            }
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
                orderRepository.addProductToCart(
                    productId = state.products[index].productId,
                    filter = state.products[index].filterList.firstOrNull()
                )
            } else {
                orderRepository.removeProductFromCart(state.products[index].productId)
            }
        }
    }

}