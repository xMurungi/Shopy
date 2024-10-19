package com.ag_apps.product.presentation.product_overview

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.core.domain.util.Result
import com.ag_apps.product.domain.ProductRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class ProductOverviewViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    var state by mutableStateOf(ProductOverviewState())
        private set

    init {
        state = state.copy(
            productsOffset = 0,
            minPriceState = TextFieldState(""),
            maxPriceState = TextFieldState(""),
        )
        loadProducts()

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

    fun onAction(action: ProductOverviewAction) {
        when (action) {
            ProductOverviewAction.Refresh -> {
                state = state.copy(
                    productsOffset = 0,
                    minPriceState = TextFieldState(""),
                    maxPriceState = TextFieldState(""),
                )
                loadProducts()
            }

            ProductOverviewAction.Paginate -> {
                state = state.copy(productsOffset = state.productsOffset + 10)
                loadProducts()
            }

            ProductOverviewAction.LoadProductsWithFilters -> {
                state = state.copy(productsOffset = 0)
                loadProducts()
            }

            ProductOverviewAction.ToggleFilter -> {
                state = state.copy(isFilterOpen = !state.isFilterOpen)
            }

            ProductOverviewAction.ToggleProductsLayout -> {
                state = state.copy(isGridLayout = !state.isGridLayout)
            }

            is ProductOverviewAction.AddProductToWishlist -> {
                addProductToWishlist(action.productId)
            }

            is ProductOverviewAction.AddProductToCart -> {
                addProductToCart(action.productId)
            }

            is ProductOverviewAction.SelectProduct -> Unit
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {

            state = state.copy(
                isLoading = true, isError = false,
            )

            val minPrice = state.minPriceState.text.toString()
            val maxPrice = state.maxPriceState.text.toString()

            val productsResult = productRepository.getProducts(
                offset = state.productsOffset,
                minPrice = if (minPrice.isNotBlank()) minPrice.toIntOrNull() else null,
                maxPrice = if (minPrice.isNotBlank()) maxPrice.toIntOrNull() else null
            )

            when (productsResult) {
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        isError = true
                    )
                }

                is Result.Success -> {
                    state = state.copy(
                        isLoading = false,
                        isError = false,
                        products = productsResult.data
                    )
                }
            }
        }
    }

    private fun addProductToWishlist(productId: Int) {
        viewModelScope.launch {
            productRepository.addProductToWishlist(productId.toString())
            state = state.copy(
                products = state.products.map { product ->
                    if (product.productId == productId) {
                        product.copy(isInWishList = true)
                    } else {
                        product
                    }
                }
            )
        }
    }

    private fun addProductToCart(productId: Int) {
        viewModelScope.launch {
            productRepository.addProductToCart(productId.toString())
            state = state.copy(
                products = state.products.map { product ->
                    if (product.productId == productId) {
                        product.copy(isInCartList = true)
                    } else {
                        product
                    }
                }
            )
        }
    }

}