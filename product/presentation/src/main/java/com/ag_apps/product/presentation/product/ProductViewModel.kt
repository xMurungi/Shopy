package com.ag_apps.product.presentation.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.core.domain.util.Result
import com.ag_apps.product.domain.ProductRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    var state by mutableStateOf(ProductState())
        private set

    private val eventChannel = Channel<ProductEvent>()
    val event = eventChannel.receiveAsFlow()

    fun onAction(action: ProductAction) {
        when (action) {
            is ProductAction.LoadProduct -> {
                state = state.copy(productId = action.productId)
                loadProduct(action.productId)
            }

            is ProductAction.SelectFilter -> {
                state = state.copy(selectedFilter = action.selectedFilter)
                updateProductInCart()
            }

            is ProductAction.ToggleProductInWishlist -> {
                toggleProductInWishlist()
            }

            is ProductAction.ToggleProductInCart -> {
                toggleProductInCart()
            }

            ProductAction.GoBack -> Unit

            ProductAction.Refresh -> {
                state.productId?.let {
                    loadProduct(it)
                }
            }
        }
    }

    private fun loadProduct(productId: Int) {
        viewModelScope.launch {

            state = state.copy(
                isLoading = true, isError = false,
            )

            state = when (val productResult = productRepository.getProduct(productId)) {
                is Result.Error -> {
                    state.copy(
                        isLoading = false, isError = true
                    )
                }

                is Result.Success -> {
                    state.copy(
                        isLoading = false,
                        isError = false,
                        product = productResult.data,
                        selectedFilter = productResult.data.selectedFilter
                    )
                }
            }
        }
    }

    private fun toggleProductInWishlist() {
        viewModelScope.launch {

            state.product?.let { product ->
                state = state.copy(
                    product = product.copy(isInWishList = !product.isInWishList),
                    isProductUpdate = true
                )

                if (!product.isInWishList) {
                    productRepository.addProductToWishlist(product.productId)
                } else {
                    productRepository.removeProductFromWishlist(product.productId)
                }
            }
        }
    }

    private fun toggleProductInCart() {
        viewModelScope.launch {

            state.product?.let { product ->
                state = state.copy(
                    product = product.copy(isInCartList = !product.isInCartList)
                )

                if (!product.isInCartList) {
                    productRepository.addProductToCart(
                        productId = product.productId,
                        filter = state.selectedFilter
                    )
                } else {
                    productRepository.removeProductFromCart(product.productId)
                }

                state = state.copy(isProductUpdate = true)
            }
        }
    }

    private fun updateProductInCart() {
        viewModelScope.launch {
            state.product?.let { product ->

                productRepository.removeProductFromCart(product.productId)
                productRepository.addProductToCart(
                    productId = product.productId,
                    filter = state.selectedFilter
                )

                state = state.copy(isProductUpdate = true)
            }
        }
    }

}