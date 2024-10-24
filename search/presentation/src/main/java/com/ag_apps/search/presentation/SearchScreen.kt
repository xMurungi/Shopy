package com.ag_apps.search.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.core.presentation.ProductList
import com.ag_apps.core.presentation.ProductsScaffold
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.util.previewProducts
import org.koin.androidx.compose.koinViewModel

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun SearchScreenCore(
    viewModel: SearchViewModel = koinViewModel(),
    appName: String,
    updatedProductId: Int?,
    onProductClick: (Int) -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        println("SearchScreenCore: updatedProductId $updatedProductId")

        if (updatedProductId != null) {
            viewModel.onAction(SearchAction.RefreshUpdatedProductFromDetails(updatedProductId))
        }
    }

    SearchScreen(
        state = viewModel.state,
        appName = appName,
        onAction = { action ->
            when (action) {
                is SearchAction.ClickProduct -> {
                    onProductClick(
                        viewModel.state.products[action.productIndex].productId
                    )
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun SearchScreen(
    state: SearchState,
    appName: String,
    onAction: (SearchAction) -> Unit,
) {

    ProductsScaffold(
        appName = appName,
        isForSearch = true,
        searchQueryState = state.searchQueryState,
        isFilterOpen = state.isFilterOpen,
        minPriceState = state.minPriceState,
        maxPriceState = state.maxPriceState,
        toggleFilter = { onAction(SearchAction.ToggleFilter) },
        toggleProductsLayout = { onAction(SearchAction.ToggleProductsLayout) },
        applyFilter = { onAction(SearchAction.ApplyFilter) },
    ) { padding ->
        ProductList(
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
            products = state.products,
            isGridLayout = state.isGridLayout,
            isLoading = state.isLoading,
            categories = state.categories,
            isApplyingFilter = state.isApplyingFilter,
            onToggleProductInWishlist = { index ->
                onAction(SearchAction.ToggleProductInWishlist(index))
            },
            onToggleProductInCart = { index ->
                onAction(SearchAction.ToggleProductInCart(index))
            },
            onPaginate = {
                onAction(SearchAction.Paginate)
            },
            onProductClick = { index ->
                onAction(SearchAction.ClickProduct(index))
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.isApplyingFilter || state.isLoading && !state.isError && state.products.isEmpty()) {
                CircularProgressIndicator()
            }
            if (state.isError && state.products.isEmpty()) {
                Text(
                    text = "Can't search products right now.",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }

}

@Preview
@Composable
private fun SearchScreenPreview() {
    ShopyTheme {
        SearchScreen(
            appName = "Shopy",
            state = SearchState(
                products = previewProducts,
                isGridLayout = true
            ),
            onAction = {}
        )
    }
}
