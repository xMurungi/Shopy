package com.ag_apps.product.presentation.product_overview

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.core.domain.Product
import com.ag_apps.core.presentation.ProductItem
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.ui.ObserveAsEvent
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
            if (state.isGridLayout) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding())
                        .padding(horizontal = 6.dp),
                    columns = GridCells.Fixed(2),
                ) {
                    itemsIndexed(state.products) { index, product ->
                        ProductItem(
                            modifier = Modifier
                                .padding(6.dp)
                                .padding(top = if (index == 0 || index == 1) 8.dp else 0.dp),
                            product = product,
                            isGrid = true,
                            onAddToWishlist = {
                                onAction(ProductOverviewAction.ToggleProductInWishlist(index))
                            },
                            onAddToCart = {
                                onAction(ProductOverviewAction.ToggleProductInCart(index))
                            },
                            onClick = {
                                onAction(ProductOverviewAction.SelectProduct(index))
                            }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(state.products) { index, product ->
                        ProductItem(
                            modifier = Modifier.height(120.dp),
                            product = product,
                            isGrid = false,
                            imageWidth = 120.dp,
                            onAddToWishlist = {
                                onAction(ProductOverviewAction.ToggleProductInWishlist(index))
                            },
                            onAddToCart = {
                                onAction(ProductOverviewAction.ToggleProductInCart(index))
                            },
                            onClick = {
                                onAction(ProductOverviewAction.SelectProduct(index))
                            }
                        )

                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
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