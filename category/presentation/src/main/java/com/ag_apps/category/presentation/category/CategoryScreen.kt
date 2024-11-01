package com.ag_apps.category.presentation.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.category.presentation.R
import com.ag_apps.core.presentation.OnResume
import com.ag_apps.core.presentation.ProductList
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyScaffold
import com.ag_apps.core.presentation.designsystem.components.ShopyTopBar
import com.ag_apps.core.presentation.util.PreviewProducts
import org.koin.androidx.compose.koinViewModel

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun CategoryScreenCore(
    viewModel: CategoryViewModel = koinViewModel(),
    categoryId: Int,
    onProductClick: (Int) -> Unit,
    onSearch: () -> Unit,
    onBack: () -> Unit
) {
    OnResume {
        viewModel.onAction(CategoryAction.RefreshUpdatedProducts)
    }

    LaunchedEffect(Unit) {
        viewModel.onAction(CategoryAction.LoadCategoryProducts(categoryId))
    }

    CategoryScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is CategoryAction.ClickProduct -> {
                    onProductClick(
                        viewModel.state.products[action.productIndex].productId
                    )
                }

                is CategoryAction.Search -> onSearch()

                is CategoryAction.Back -> onBack()

                else -> viewModel.onAction(action)

            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryScreen(
    state: CategoryState,
    onAction: (CategoryAction) -> Unit,
) {

    ShopyScaffold(
        topBar = { scrollBehavior ->
            ShopyTopBar(
                scrollBehavior = scrollBehavior,
                titleText = state.category?.name,
                actionIcon = Icons.Rounded.Search,
                actionIconDescription = stringResource(R.string.search_products),
                onActionClick = { onAction(CategoryAction.Search) },
                navigationIcon = Icons.Rounded.ArrowBackIosNew,
                navigationIconDescription = stringResource(R.string.go_back),
                onNavigationClick = { onAction(CategoryAction.Back) },
            )
        }
    ) { padding ->
        ProductList(
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
            products = state.products,
            isGridLayout = state.isGridLayout,
            onToggleProductInWishlist = { index ->
                onAction(CategoryAction.ToggleProductInWishlist(index))
            },
            onToggleProductInCart = { index ->
                onAction(CategoryAction.ToggleProductInCart(index))
            },
            onProductClick = { index ->
                onAction(CategoryAction.ClickProduct(index))
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading && !state.isError && state.products.isEmpty()) {
                CircularProgressIndicator()
            }
            if (state.isError && state.products.isEmpty()) {
                Text(
                    text = stringResource(R.string.can_t_load_products_right_now),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
            if (!state.isLoading && !state.isError && state.products.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_products_found_in_this_category),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.7f),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CategoryScreenPreview() {
    ShopyTheme {
        CategoryScreen(
            state = CategoryState(
                products = PreviewProducts,
                isGridLayout = true
            ),
            onAction = {}
        )
    }
}
