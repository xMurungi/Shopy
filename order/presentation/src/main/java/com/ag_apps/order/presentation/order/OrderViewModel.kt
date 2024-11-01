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
                loadOrder(action.orderId)
            }

            is OrderAction.OnProductClick -> Unit

            OrderAction.OnBackClick -> Unit
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
                        isLoading = false,
                        order = orderResult.data
                    )

                    loadOrderProducts(orderResult.data.products)
                }
            }
        }
    }

    private fun loadOrderProducts(productsMap: Map<Int, String?>) {
        viewModelScope.launch {
            when (val productsResult = orderRepository.getOrderProducts(productsMap)) {
                is Result.Error -> Unit
                is Result.Success -> {
                    state = state.copy(
                        products = productsResult.data
                    )
                }
            }
        }
    }

}