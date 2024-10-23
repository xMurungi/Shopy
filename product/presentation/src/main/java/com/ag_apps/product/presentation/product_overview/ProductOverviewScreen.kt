package com.ag_apps.product.presentation.product_overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.core.presentation.ProductList
import com.ag_apps.core.presentation.ProductsScaffold
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.model.ProductUI
import com.ag_apps.core.presentation.model.previewProducts
import com.ag_apps.product.presentation.R
import org.koin.androidx.compose.koinViewModel

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun ProductOverviewScreenCore(
    viewModel: ProductOverviewViewModel = koinViewModel(),
    appName: String,
    onProductClick: (Int) -> Unit,
    onSearch: () -> Unit
) {

    ProductOverviewScreen(
        state = viewModel.state,
        appName = appName,
        onAction = { action ->
            when (action) {
                is ProductOverviewAction.ClickProduct -> {
                    onProductClick(
                        viewModel.state.products[action.productIndex].productId
                    )
                }

                is ProductOverviewAction.Search -> onSearch()

                else -> viewModel.onAction(action)

            }
        }
    )
}

@Composable
private fun ProductOverviewScreen(
    state: ProductOverviewState,
    appName: String,
    onAction: (ProductOverviewAction) -> Unit,
) {

    ProductsScaffold(
        appName = appName,
        isFilterOpen = state.isFilterOpen,
        minPriceState = state.minPriceState,
        maxPriceState = state.maxPriceState,
        toggleFilter = { onAction(ProductOverviewAction.ToggleFilter) },
        toggleProductsLayout = { onAction(ProductOverviewAction.ToggleProductsLayout) },
        applyFilter = { onAction(ProductOverviewAction.ApplyFilter) },
        onSearch = { onAction(ProductOverviewAction.Search) },
    ) { padding ->
        ProductList(
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
            products = state.products,
            isGridLayout = state.isGridLayout,
            isLoading = state.isLoading,
            categories = state.categories,
            isApplyingFilter = state.isApplyingFilter,
            onToggleProductInWishlist = { index ->
                onAction(ProductOverviewAction.ToggleProductInWishlist(index))
            },
            onToggleProductInCart = { index ->
                onAction(ProductOverviewAction.ToggleProductInCart(index))
            },
            onPaginate = {
                onAction(ProductOverviewAction.Paginate)
            },
            onProductClick = { index ->
                onAction(ProductOverviewAction.ClickProduct(index))
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
                    text = stringResource(R.string.can_t_load_products_right_now_please_try_again_later),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }

}

@Preview
@Composable
private fun ProductOverviewScreenPreview() {
    ShopyTheme {
        ProductOverviewScreen(
            appName = "Shopy",
            state = ProductOverviewState(
                products = previewProducts,
                isGridLayout = true
            ),
            onAction = {}
        )
    }
}
