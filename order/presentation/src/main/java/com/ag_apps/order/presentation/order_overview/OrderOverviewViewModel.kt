package com.ag_apps.order.presentation.order_overview

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
class OrderOverviewViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    var state by mutableStateOf(OrderOverviewState())
        private set

    init {
        loadOrders()
    }

    fun onAction(action: OrderOverviewAction) {
        when (action) {
            is OrderOverviewAction.OnOrderClick -> Unit
            OrderOverviewAction.OnBackClick -> Unit
            OrderOverviewAction.Refresh -> loadOrders()
        }
    }

    private fun loadOrders() {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            state = when (val ordersResult = orderRepository.getOrders()) {
                is Result.Error -> {
                    state.copy(
                        isError = true,
                        isLoading = false
                    )
                }

                is Result.Success -> {
                    state.copy(
                        isError = false,
                        isLoading = false,
                        orders = ordersResult.data
                    )
                }
            }
        }
    }

}