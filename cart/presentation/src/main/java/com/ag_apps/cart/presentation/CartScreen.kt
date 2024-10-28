package com.ag_apps.cart.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.core.presentation.ProductList
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyButton
import com.ag_apps.core.presentation.designsystem.components.ShopyScaffold
import com.ag_apps.core.presentation.designsystem.components.ShopyTopBar
import com.ag_apps.core.presentation.util.previewProducts
import org.koin.androidx.compose.koinViewModel

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun CartScreenCore(
    viewModel: CartViewModel = koinViewModel(),
    onProductClick: (Int) -> Unit,
    onCheckout: () -> Unit,
) {


    LaunchedEffect(true) {
        viewModel.onAction(CartAction.Refresh)
    }

    CartScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is CartAction.ClickProduct -> {
                    onProductClick(
                        viewModel.state.products[action.productIndex].productId
                    )
                }

                is CartAction.Checkout -> {
                    onCheckout()
                }

                else -> viewModel.onAction(action)

            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartScreen(
    state: CartState,
    onAction: (CartAction) -> Unit
) {
    ShopyScaffold(
        topBar = { scrollBehavior ->
            ShopyTopBar(
                scrollBehavior = scrollBehavior,
                titleText = stringResource(R.string.my_cart),
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ProductList(
                modifier = Modifier.padding(top = padding.calculateTopPadding()),
                products = state.products,
                isGridLayout = false,
                isLoading = state.isLoading,
                contentPadding = PaddingValues(bottom = 150.dp),
                onToggleProductInWishlist = { index ->
                    onAction(CartAction.ToggleProductInWishlist(index))
                },
                onProductClick = { index ->
                    onAction(CartAction.ClickProduct(index))
                }
            )

            if (state.products.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .align(Alignment.BottomCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.total_price),
                            fontSize = 14.sp
                        )

                        Text(
                            text = "$${state.totalPrice}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    ShopyButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = stringResource(R.string.check_out),
                        onClick = {
                            onAction(CartAction.Checkout)
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                }
            }

            if (state.isLoading && !state.isError && state.products.isEmpty()) {
                CircularProgressIndicator()
            }
            if (state.isError && state.products.isEmpty()) {
                Text(
                    text = stringResource(R.string.can_t_load_cart_right_now),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CartScreenPreview() {
    ShopyTheme {
        CartScreen(
            state = CartState(
                products = previewProducts
            ),
            onAction = {}
        )
    }
}