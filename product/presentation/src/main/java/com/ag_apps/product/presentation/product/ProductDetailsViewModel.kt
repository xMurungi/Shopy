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
class ProductDetailsViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    var state by mutableStateOf(ProductDetailsState())
        private set

    private val eventChannel = Channel<ProductDetailsEvent>()
    val event = eventChannel.receiveAsFlow()

    fun onAction(action: ProductDetailsAction) {
        when (action) {
            is ProductDetailsAction.LoadProduct -> {
                loadProduct(action.productId)
            }

            is ProductDetailsAction.SelectFilter -> {
                state = state.copy(
                    selectedFilterIndex = action.filterIndex
                )
            }

            is ProductDetailsAction.ToggleProductInWishlist -> {
                toggleProductInWishlist()
            }

            is ProductDetailsAction.ToggleProductInCart -> {
                toggleProductInCart()
            }

            ProductDetailsAction.GoBack -> Unit
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
                        product = productResult.data
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
                    product = product.copy(isInCartList = !product.isInCartList),
                    isProductUpdate = true
                )

                if (!product.isInCartList) {
                    productRepository.addProductToCart(
                        productId = product.productId,
                        filter = product.filterList[state.selectedFilterIndex ?: 0]
                    )
                } else {
                    productRepository.removeProductFromCart(product.productId)
                }
            }
        }
    }

}