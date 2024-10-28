package com.ag_apps.category.presentation.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.category.domain.CategoryRepository
import com.ag_apps.core.domain.util.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class CategoryViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    var state by mutableStateOf(CategoryState())
        private set

    private val eventChannel = Channel<CategoryEvent>()
    val event = eventChannel.receiveAsFlow()

    private var loadCategoryProductsAttempts = 0
    private var refreshUpdatedProductsAttempts = 0

    fun onAction(action: CategoryAction) {
        when (action) {
            is CategoryAction.LoadCategoryProducts -> {
                if (loadCategoryProductsAttempts == 0) {
                    loadCategoryProductsAttempts++
                    state = state.copy(categoryId = action.categoryId)
                    loadCategoryProducts()
                }
            }

            CategoryAction.Refresh -> {
                loadCategoryProducts()
            }

            is CategoryAction.RefreshUpdatedProducts -> {
                if (state.products.isNotEmpty() && refreshUpdatedProductsAttempts > 0) {
                    refreshUpdatedProducts()
                }
                refreshUpdatedProductsAttempts++
            }

            CategoryAction.ToggleProductsLayout -> {
                state = state.copy(isGridLayout = !state.isGridLayout)
            }

            is CategoryAction.ToggleProductInWishlist -> {
                toggleProductInWishlist(action.productIndex)
            }

            is CategoryAction.ToggleProductInCart -> {
                toggleProductInCart(action.productIndex)
            }

            is CategoryAction.ClickProduct -> Unit

            CategoryAction.Search -> Unit

            CategoryAction.Back -> Unit
        }
    }

    private fun refreshUpdatedProducts() {
        println("categoryViewModel: refreshUpdatedProducts")
        viewModelScope.launch {
            state.categoryId?.let { categoryId ->
                val productsResult = categoryRepository.getCategoryProducts(
                    categoryId
                )

                when (productsResult) {

                    is Result.Success -> {

                        val updatedProductsMap = productsResult.data.map {
                            it.productId to it
                        }.toMap()

                        state = state.copy(
                            products = state.products.map { product ->
                                updatedProductsMap[product.productId]?.copy(
                                    thumbnail = product.thumbnail
                                ) ?: product
                            }
                        )

                    }

                    is Result.Error -> Unit
                }
            }
        }
    }

    private fun loadCategoryProducts() {
        println("categoryViewModel: loadCategoryProducts")
        viewModelScope.launch {
            state.categoryId?.let { categoryId ->
                state = state.copy(
                    isLoading = true, isError = false,
                )

                loadCategory(categoryId)

                when (val productsResult = categoryRepository.getCategoryProducts(categoryId)) {
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
    }

    private fun loadCategory(categoryId: Int) {
        viewModelScope.launch {
            when (val categoryResult = categoryRepository.getCategory(categoryId)) {
                is Result.Error -> Unit
                is Result.Success -> {
                    state = state.copy(category = categoryResult.data)
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
                categoryRepository.addProductToWishlist(state.products[index].productId)
            } else {
                categoryRepository.removeProductFromWishlist(state.products[index].productId)
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
                categoryRepository.addProductToCart(
                    productId = state.products[index].productId,
                    filter = state.products[index].filterList.firstOrNull(),
                )
            } else {
                categoryRepository.removeProductFromCart(state.products[index].productId)
            }
        }
    }

}