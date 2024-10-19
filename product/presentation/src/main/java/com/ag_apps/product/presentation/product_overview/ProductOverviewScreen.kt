package com.ag_apps.product.presentation.product_overview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ag_apps.core.domain.Product
import com.ag_apps.core.presentation.ProductItem
import com.ag_apps.core.presentation.designsystem.ShopyTheme
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
                onSelectedProduct(action.productId)
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

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(appName) },
                windowInsets = WindowInsets(top = 0.dp)
            )
        }
    ) { padding ->
        if (state.isGridLayout) {
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 6.dp),
                columns = GridCells.Fixed(2),
            ) {
                itemsIndexed(state.products) { index, product ->
                    ProductItem(
                        modifier = Modifier.padding(6.dp),
                        product = product,
                        isGrid = true,
                        onAddToWishlist = {
                            onAction(ProductOverviewAction.AddProductToWishlist(product.productId))
                        },
                        onAddToCart = {
                            onAction(ProductOverviewAction.AddProductToCart(product.productId))
                        },
                        onClick = {
                            onAction(ProductOverviewAction.SelectProduct(product.productId))
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
                            onAction(ProductOverviewAction.AddProductToWishlist(product.productId))
                        },
                        onAddToCart = {
                            onAction(ProductOverviewAction.AddProductToCart(product.productId))
                        },
                        onClick = {
                            onAction(ProductOverviewAction.SelectProduct(product.productId))
                        }
                    )

                    Spacer(Modifier.height(12.dp))
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