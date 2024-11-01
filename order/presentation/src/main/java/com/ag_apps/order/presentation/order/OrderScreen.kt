package com.ag_apps.order.presentation.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.core.domain.Address
import com.ag_apps.core.domain.Order
import com.ag_apps.core.presentation.ProductList
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyScaffold
import com.ag_apps.core.presentation.designsystem.components.ShopyTopBar
import com.ag_apps.core.presentation.util.PreviewProducts
import com.ag_apps.order.presentation.R
import com.ag_apps.order.presentation.util.toFormattedDate
import org.koin.androidx.compose.koinViewModel

/**
 * @author Ahmed Guedmioui
 */
@Composable
fun OrderScreenCore(
    viewModel: OrderViewModel = koinViewModel(),
    orderId: Int,
    onProductClick: (productId: Int) -> Unit,
    onBackClick: () -> Unit,
) {

    LaunchedEffect(true) {
        viewModel.onAction(OrderAction.LoadOrder(orderId))
    }

    OrderScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is OrderAction.OnProductClick -> {
                    onProductClick(action.productId)
                }

                OrderAction.OnBackClick -> {
                    onBackClick()
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderScreen(
    state: OrderState,
    onAction: (OrderAction) -> Unit
) {
    ShopyScaffold(
        topBar = { scrollBehavior ->
            ShopyTopBar(
                scrollBehavior = scrollBehavior,
                titleText = stringResource(R.string.order_details),
                navigationIcon = Icons.Rounded.ArrowBackIosNew,
                onNavigationClick = { onAction(OrderAction.OnBackClick) }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 5.dp,
                            spotColor = MaterialTheme.colorScheme.onBackground
                        )
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Order: №${state.order?.orderId}",
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${state.order?.date?.toFormattedDate()}",
                        fontWeight = FontWeight.Medium
                    )
                }
                ProductList(
                    products = state.products,
                    isGridLayout = false,
                    onProductClick = { onAction(OrderAction.OnProductClick(it)) },
                )
            }


            OrderInformation(
                state = state,
                modifier = Modifier.align(Alignment.BottomCenter)
            )

        }

    }
}

@Composable
fun OrderInformation(
    modifier: Modifier = Modifier,
    state: OrderState
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(vertical = 16.dp)
            .padding(start = 16.dp, end = 8.dp)
    ) {

        Text(
            text = "Order Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(16.dp))

        Row {
            Text(
                text = "Shipping Address:",
                fontSize = 14.sp
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "${state.order?.address?.street}, ${state.order?.address?.city}, ${state.order?.address?.region}, ${state.order?.address?.zipCode}, ${state.order?.address?.country}",
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(12.dp))

        Row {
            Text(
                text = "Total Price:",
                fontSize = 14.sp
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "$${state.order?.totalPrice}",
                fontWeight = FontWeight.Medium
            )
        }

    }
}

@Preview
@Composable
private fun OrderScreenPreview() {
    ShopyTheme {
        OrderScreen(
            state = OrderState(
                order = Order(
                    orderId = 243,
                    date = 1234567890,
                    totalPrice = 100.0,
                    address = Address(
                        street = "Hay Salam",
                        city = "Agadir",
                        region = "Sous-Massa",
                        country = "Morocco",
                        zipCode = "80000",
                    ),
                    products = mapOf()
                ),
                products = PreviewProducts
            ),
            onAction = {}
        )
    }
}