package com.ag_apps.wishlist.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
fun WishlistScreenCore(
    viewModel: WishlistViewModel = koinViewModel(),
    onProductClick: (Int) -> Unit,
    onSearch: () -> Unit
) {


    LaunchedEffect(true) {
        viewModel.onAction(WishlistAction.Refresh)
    }

    WishlistScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is WishlistAction.ClickProduct -> {
                    onProductClick(
                        viewModel.state.products[action.productIndex].productId
                    )
                }

                is WishlistAction.Search -> onSearch()

                else -> viewModel.onAction(action)

            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WishlistScreen(
    state: WishlistState,
    onAction: (WishlistAction) -> Unit
) {
    ShopyScaffold(
        topBar = { scrollBehavior ->
            ShopyTopBar(
                scrollBehavior = scrollBehavior,
                titleText = stringResource(R.string.wishlist),
                actionIcon = Icons.Rounded.Search,
                actionIconDescription = stringResource(R.string.search_products),
                onActionClick = { onAction(WishlistAction.Search) },
            )
        },
        onRefresh = {
            onAction(WishlistAction.Refresh)
        }
    ) { padding ->
        if (state.products.isNotEmpty()) {
            ProductList(
                modifier = Modifier.padding(top = padding.calculateTopPadding()),
                products = state.products,
                isGridLayout = false,
                onRemove = { index ->
                    onAction(WishlistAction.RemoveProductFromWishlist(index))
                },
                onToggleProductInCart = { index ->
                    onAction(WishlistAction.ToggleProductInCart(index))
                },
                onProductClick = { index ->
                    onAction(WishlistAction.ClickProduct(index))
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading && !state.isError && state.products.isEmpty()) {
                    CircularProgressIndicator()
                }
                if (state.isError && state.products.isEmpty()) {
                    Text(
                        text = stringResource(R.string.can_t_load_wishlist_right_now),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                }
                if (!state.isLoading && !state.isError && state.products.isEmpty()) {
                    Text(
                        text = stringResource(R.string.your_wishlist_is_empty),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.7f),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

    }
}

@Preview
@Composable
private fun WishlistScreenPreview() {
    ShopyTheme {
        WishlistScreen(
            state = WishlistState(
                products = PreviewProducts
            ),
            onAction = {}
        )
    }
}