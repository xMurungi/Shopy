package com.ag_apps.product.presentation.product_overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRightAlt
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.ArrowRight
import androidx.compose.material.icons.automirrored.rounded.ArrowRightAlt
import androidx.compose.material.icons.automirrored.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.core.domain.Product
import com.ag_apps.core.presentation.ProductList
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyOutlinedButton
import com.ag_apps.core.presentation.designsystem.components.ShopyTextField
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductOverviewScreen(
    state: ProductOverviewState,
    appName: String = "Shopy",
    onAction: (ProductOverviewAction) -> Unit,
) {


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerLow.copy(0.6f))
                    .padding(bottom = 8.dp)
            ) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(0.6f),
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(0.6f),
                    ),
                    scrollBehavior = scrollBehavior,
                    title = { Text(appName) },
                    actions = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = stringResource(R.string.search_for_products),
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(30.dp)
                                .clickable {
                                    onAction(ProductOverviewAction.Search)
                                }
                        )
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable {
                                onAction(ProductOverviewAction.ToggleFilter)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.FilterList,
                            contentDescription = stringResource(R.string.search_for_products),
                            modifier = Modifier
                                .size(30.dp)
                        )

                        Spacer(Modifier.width(4.dp))

                        Text(stringResource(R.string.filter))
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.FormatListBulleted,
                        contentDescription = stringResource(R.string.search_for_products),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .size(28.dp)
                            .clickable {
                                onAction(ProductOverviewAction.ToggleProductsLayout)
                            }
                    )
                }

                if (state.isFilterOpen) {

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ShopyTextField(
                            textFieldState = state.minPriceState,
                            hint = stringResource(R.string.min_price),
                            applyTextWeight = false,
                            textVerticalPadding = 10.dp,
                            textSize = 15.sp,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowRightAlt,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = stringResource(R.string.search_for_products),
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(28.dp)
                        )

                        ShopyTextField(
                            textFieldState = state.maxPriceState,
                            hint = stringResource(R.string.max_price),
                            applyTextWeight = false,
                            textVerticalPadding = 10.dp,
                            textSize = 15.sp,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(Modifier.width(16.dp))

                        Text(
                            text = stringResource(R.string.apply),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable {
                                    onAction(ProductOverviewAction.ApplyFilter)
                                }
                        )
                    }
                }

            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.isApplyingFilter || state.isLoading && !state.isError) {
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

        if (state.products.isNotEmpty() && !state.isApplyingFilter) {
            ProductList(
                modifier = Modifier.padding(top = padding.calculateTopPadding()),
                products = state.products,
                isGridLayout = state.isGridLayout,
                isLoading = state.isLoading,
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