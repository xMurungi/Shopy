package com.ag_apps.order.presentation.order_overview

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.core.domain.Order
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyLargeTopBar
import com.ag_apps.core.presentation.designsystem.components.ShopyOutlinedButton
import com.ag_apps.core.presentation.designsystem.components.ShopyScaffold
import com.ag_apps.order.presentation.R
import com.ag_apps.order.presentation.util.toFormattedDate
import org.koin.androidx.compose.koinViewModel

/**
 * @author Ahmed Guedmioui
 */
@Composable
fun OrderOverviewScreenCore(
    viewModel: OrderOverviewViewModel = koinViewModel(),
    onOrderClick: (Int) -> Unit,
    onBackClick: () -> Unit,
) {
    OrderOverviewScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is OrderOverviewAction.OnOrderClick -> {
                    onOrderClick(action.orderIndex)
                }

                OrderOverviewAction.OnBackClick -> {
                    onBackClick()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderOverviewScreen(
    state: OrderOverviewState,
    onAction: (OrderOverviewAction) -> Unit
) {

    ShopyScaffold(
        topBar = { scrollBehavior ->
            ShopyLargeTopBar(
                scrollBehavior = scrollBehavior,
                titleText = stringResource(R.string.my_orders),
                navigationIcon = Icons.Outlined.ArrowBackIosNew,
                onNavigationClick = {
                    onAction(OrderOverviewAction.OnBackClick)
                }
            )
        }
    ) { padding ->
        if (state.orders.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(state.orders.size) { index ->
                    OrderOverviewItem(
                        index = index,
                        order = state.orders[index],
                        onAction = onAction
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading && !state.isError && state.orders.isEmpty()) {
                CircularProgressIndicator()
            }
            if (state.isError && state.orders.isEmpty()) {
                Text(
                    text = stringResource(R.string.can_t_load_orders_right_now),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
            if (!state.isLoading && !state.isError && state.orders.isEmpty()) {
                Text(
                    text = stringResource(R.string.you_have_no_orders_yet),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.7f),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun OrderOverviewItem(
    index: Int,
    order: Order,
    modifier: Modifier = Modifier,
    onAction: (OrderOverviewAction) -> Unit
) {

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(20.dp),
    ) {

        Text(
            text = stringResource(R.string.order, index),
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = stringResource(R.string.items),
                    fontSize = 13.sp
                )
                Text(
                    text = " ${order.products.size}",
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.weight(1.3f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = stringResource(R.string.total_price),
                    fontSize = 13.sp
                )
                Text(
                    text = " $${order.totalPrice}",
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }


        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = stringResource(R.string.success),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
                Text(
                    text = order.date.toFormattedDate(),
                    fontSize = 13.sp
                )
            }

            ShopyOutlinedButton(
                text = stringResource(R.string.details),
                verticalPadding = 0.dp,
                onClick = {
                    onAction(OrderOverviewAction.OnOrderClick(index))
                }
            )
        }

    }

}

@Preview
@Composable
private fun OrderOverviewScreenPreview() {
    ShopyTheme {
        OrderOverviewScreen(
            state = OrderOverviewState(
                orders = listOf(
                    Order(
                        orderId = 11,
                        date = 1234567890,
                        totalPrice = 100.0,
                        address = null,
                        products = mapOf()
                    ),
                    Order(
                        orderId = 11,
                        date = 1234567890,
                        totalPrice = 100.0,
                        address = null,
                        products = mapOf()
                    ),
                    Order(
                        orderId = 11,
                        date = 1234567890,
                        totalPrice = 100.0,
                        address = null,
                        products = mapOf()
                    ),
                    Order(
                        orderId = 11,
                        date = 1234567890,
                        totalPrice = 100.0,
                        address = null,
                        products = mapOf()
                    )
                )
            ),
            onAction = {}
        )
    }
}