package com.ag_apps.product.presentation.product_overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.core.domain.Product
import com.ag_apps.core.presentation.ProductList
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.product.presentation.R
import org.koin.androidx.compose.koinViewModel

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun ProductOverviewScreenCore(
    viewModel: ProductOverviewViewModel = koinViewModel(),
    appName: String,
    onSelectedProduct: (Int) -> Unit
) {

    ProductOverviewScreen(
        state = viewModel.state,
        appName = appName,
        onAction = { action ->
            if (action is ProductOverviewAction.SelectProduct) {
                onSelectedProduct(
                    viewModel.state.products[action.productIndex].productId
                )
            } else {
                viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductOverviewScreen(
    state: ProductOverviewState,
    appName: String = "Shopy",
    onAction: (ProductOverviewAction) -> Unit
) {


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(0.4f),
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(
                        0.4f
                    ),
                ),
                scrollBehavior = scrollBehavior,
                title = { Text(appName) },
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading && !state.isError) {
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

        if (state.products.isNotEmpty()) {
            ProductList(
                modifier = Modifier
                    .padding(top = padding.calculateTopPadding()),
                isGridLayout = !state.isGridLayout,
                products = state.products,
                onToggleProductInWishlist = { index ->
                    onAction(ProductOverviewAction.ToggleProductInWishlist(index))
                },
                onToggleProductInCart = { index ->
                    onAction(ProductOverviewAction.ToggleProductInCart(index))
                },
                onProductClick = { index ->
                    onAction(ProductOverviewAction.SelectProduct(index))
                }
            )
        }
    }
}

@Preview
@Composable
private fun ProductOverviewScreenPreview() {
    ShopyTheme {
        ProductOverviewScreen(
            state = ProductOverviewState(
                products = products,
                isGridLayout = true
            ),
            onAction = {}
        )
    }
}

val products = listOf(
    Product(
        productId = 1,
        title = "Product 1",
        description = "Product 1 description",
        images = listOf(),
        price = 100,
        categoryName = "Category 1",
        isInWishList = false,
        isInCartList = false
    ),
    Product(
        productId = 2,
        title = "Product 2",
        description = "Product 2 description",
        images = listOf(),
        price = 230,
        categoryName = "Category 2",
        isInWishList = true,
        isInCartList = false
    ),
    Product(
        productId = 1,
        title = "Product 1",
        description = "Product 1 description",
        images = listOf(),
        price = 100,
        categoryName = "Category 1",
        isInWishList = false,
        isInCartList = false
    ),
    Product(
        productId = 2,
        title = "Product 2",
        description = "Product 2 description",
        images = listOf(),
        price = 230,
        categoryName = "Category 2",
        isInWishList = true,
        isInCartList = false
    ),
    Product(
        productId = 1,
        title = "Product 1",
        description = "Product 1 description",
        images = listOf(),
        price = 100,
        categoryName = "Category 1",
        isInWishList = false,
        isInCartList = false
    ),
    Product(
        productId = 2,
        title = "Product 2",
        description = "Product 2 description",
        images = listOf(),
        price = 230,
        categoryName = "Category 2",
        isInWishList = true,
        isInCartList = false
    ),
    Product(
        productId = 1,
        title = "Product 1",
        description = "Product 1 description",
        images = listOf(),
        price = 100,
        categoryName = "Category 1",
        isInWishList = false,
        isInCartList = false
    ),
    Product(
        productId = 2,
        title = "Product 2",
        description = "Product 2 description",
        images = listOf(),
        price = 230,
        categoryName = "Category 2",
        isInWishList = true,
        isInCartList = false
    ),
    Product(
        productId = 1,
        title = "Product 1",
        description = "Product 1 description",
        images = listOf(),
        price = 100,
        categoryName = "Category 1",
        isInWishList = false,
        isInCartList = false
    ),
    Product(
        productId = 2,
        title = "Product 2",
        description = "Product 2 description",
        images = listOf(),
        price = 230,
        categoryName = "Category 2",
        isInWishList = true,
        isInCartList = false
    ),
    Product(
        productId = 1,
        title = "Product 1",
        description = "Product 1 description",
        images = listOf(),
        price = 100,
        categoryName = "Category 1",
        isInWishList = false,
        isInCartList = false
    ),
    Product(
        productId = 2,
        title = "Product 2",
        description = "Product 2 description",
        images = listOf(),
        price = 230,
        categoryName = "Category 2",
        isInWishList = true,
        isInCartList = false
    ),
)