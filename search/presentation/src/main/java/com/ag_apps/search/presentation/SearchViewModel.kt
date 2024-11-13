package com.ag_apps.search.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.core.domain.util.Result
import com.ag_apps.search.domain.SearchRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private val eventChannel = Channel<SearchEvent>()
    val event = eventChannel.receiveAsFlow()

    private var isFirstSearch = true
    private var searchJob: Job? = null

    init {
        state = state.copy(
            productsOffset = 0,
            minPriceState = TextFieldState(""),
            maxPriceState = TextFieldState(""),
        )

        viewModelScope.launch {
            snapshotFlow { state.searchQueryState.text }.collectLatest {
                if (!isFirstSearch) {
                    searchProducts()
                } else {
                    isFirstSearch = false
                }
            }
        }

        viewModelScope.launch {
            snapshotFlow { state.minPriceState.text }.collectLatest { input ->
                val minPrice = input.filter { it.isDigit() }
                state = state.copy(
                    minPriceState = TextFieldState(minPrice.toString())
                )
            }
        }

        viewModelScope.launch {
            snapshotFlow { state.maxPriceState.text }.collectLatest { input ->
                val minPrice = input.filter { it.isDigit() }
                state = state.copy(
                    maxPriceState = TextFieldState(minPrice.toString())
                )
            }
        }
    }

    fun onAction(action: SearchAction) {
        when (action) {

            is SearchAction.RefreshUpdatedProductFromDetails -> {
                if (state.products.isNotEmpty()) {
                    refreshUpdatedProductFromDetails(action.updatedProductId)
                }
            }

            SearchAction.Paginate -> {
//                state = state.copy(productsOffset = state.productsOffset + 10)
//                searchProducts(true)
            }

            SearchAction.Refresh -> {
                state = state.copy(productsOffset = 0)
                searchProducts()
            }

            SearchAction.ApplyFilter -> {
                state = state.copy(
                    productsOffset = 0,
                    isApplyingFilter = true
                )
                searchProducts()
            }

            SearchAction.ToggleFilter -> {
                state = state.copy(isFilterOpen = !state.isFilterOpen)
            }

            SearchAction.ToggleProductsLayout -> {
                state = state.copy(isGridLayout = !state.isGridLayout)
            }

            is SearchAction.ToggleProductInWishlist -> {
                toggleProductInWishlist(action.productIndex)
            }

            is SearchAction.ToggleProductInCart -> {
                toggleProductInCart(action.productIndex)
            }

            is SearchAction.ClickProduct -> Unit
        }
    }

    private fun refreshUpdatedProductFromDetails(updatedProductId: Int) {
        viewModelScope.launch {

            when (val productResult = searchRepository.getProduct(updatedProductId)) {
                is Result.Success -> {

                    state = state.copy(
                        products = state.products.map {
                            if (it.productId == updatedProductId) {
                                productResult.data
                            } else {
                                it
                            }
                        }
                    )

                }

                is Result.Error -> Unit
            }


        }
    }

    private fun searchProducts(paginate: Boolean = false) {
        if (state.searchQueryState.text.toString().isBlank()) {
            state = state.copy(products = emptyList())
            return
        }

        searchJob = null
        searchJob = viewModelScope.launch {
            delay(500)

            state = state.copy(
                isLoading = true, isError = false,
            )

            val minPrice = state.minPriceState.text.toString()
            val maxPrice = state.maxPriceState.text.toString()

            val productsResult = searchRepository.searchProducts(
                query = state.searchQueryState.text.toString(),
                offset = state.productsOffset,
                minPrice = if (minPrice.isNotBlank()) minPrice.toIntOrNull() else 1,
                maxPrice = if (maxPrice.isNotBlank()) maxPrice.toIntOrNull() else null
            )

            when (productsResult) {
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        isApplyingFilter = false,
                        isFilterOpen = false,
                        isError = true
                    )
                }

                is Result.Success -> {
                    val products = if (paginate) {
                        state.products + productsResult.data
                    } else {
                        productsResult.data
                    }

                    state = state.copy(
                        isLoading = false,
                        isApplyingFilter = false,
                        isError = false,
                        isFilterOpen = false,
                        products = products
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
                }
            )

            if (state.products[index].isInWishList) {
                searchRepository.addProductToWishlist(state.products[index].productId)
            } else {
                searchRepository.removeProductFromWishlist(state.products[index].productId)
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
                searchRepository.addProductToCart(
                    productId = state.products[index].productId,
                    filter = state.products[index].filterList.firstOrNull()
                )
            } else {
                searchRepository.removeProductFromCart(state.products[index].productId)
            }
        }
    }

}