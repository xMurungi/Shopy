package com.ag_apps.category.presentation.category_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag_apps.category.domain.CategoryRepository
import com.ag_apps.core.domain.util.Result
import kotlinx.coroutines.launch

/**
 * @author Ahmed Guedmioui
 */
class CategoryOverviewViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    var state by mutableStateOf(CategoryOverviewState())
        private set

    init {
        loadCategories()
    }

    fun onAction(action: CategoryOverviewAction) {
        when (action) {
            is CategoryOverviewAction.ClickCategory -> Unit
            CategoryOverviewAction.Search -> Unit
            CategoryOverviewAction.Refresh -> loadCategories()
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {

            state = state.copy(
                isLoading = true, isError = false,
            )

            when (val categoriesResult = categoryRepository.getCategories()) {
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
                        categories = categoriesResult.data
                    )
                }
            }
        }
    }

}