package com.ag_apps.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ag_apps.core.domain.Category
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.model.ProductUI
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

/**
 * @author Ahmed Guedmioui
 */
@Composable
fun ProductList(
    modifier: Modifier = Modifier,
    products: List<ProductUI>,
    isGridLayout: Boolean,
    isLoading: Boolean,
    category: Category? = null,
    onToggleProductInWishlist: ((Int) -> Unit)? = null,
    onToggleProductInCart: ((Int) -> Unit)? = null,
    onRemove: ((Int) -> Unit)? = null,
    onPaginate: () -> Unit,
    onProductClick: (Int) -> Unit,
) {

    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()

    val shouldPaginate = remember {
        derivedStateOf {
            val totalItems = if (isGridLayout) {
                gridState.layoutInfo.totalItemsCount
            } else {
                listState.layoutInfo.totalItemsCount
            }

            val lastVisibleIndex = if (isGridLayout) {
                gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            } else {
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            }

            lastVisibleIndex == totalItems - 1 && !isLoading
        }
    }

    LaunchedEffect(key1 = listState, key2 = gridState) {
        snapshotFlow { shouldPaginate.value }
            .distinctUntilChanged()
            .filter { it }
            .collect { onPaginate() }
    }

    if (isGridLayout) {
        LazyVerticalGrid(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            columns = GridCells.Fixed(2),
            state = gridState
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                CategoryListItem(category = category)
            }

            itemsIndexed(products) { index, product ->
                val paddingStart = if ((index % 2) == 0) 12.dp else 0.dp
                val paddingEnd = if ((index % 2) != 0) 12.dp else 0.dp
                ProductListItem(
                    modifier = Modifier.padding(
                        top = 12.dp, start = paddingStart, end = paddingEnd
                    ),
                    product = product,
                    index = index,
                    isGridLayout = true,
                    onToggleProductInWishlist = onToggleProductInWishlist,
                    onToggleProductInCart = onToggleProductInCart,
                    onRemove = onRemove,
                    onProductClick = onProductClick
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(bottom = 8.dp),
            state = listState
        ) {
            item {
                CategoryListItem(category = category)
                Spacer(Modifier.height(12.dp))
            }

            itemsIndexed(
                items = products,
                key = { _, article -> article.productId }
            ) { index, product ->

                ProductListItem(
                    modifier = Modifier
                        .height(120.dp)
                        .padding(horizontal = 16.dp),
                    product = product,
                    index = index,
                    isGridLayout = false,
                    onToggleProductInWishlist = onToggleProductInWishlist,
                    onToggleProductInCart = onToggleProductInCart,
                    onRemove = onRemove,
                    onProductClick = onProductClick
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun CategoryListItem(
    modifier: Modifier = Modifier,
    category: Category?
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.onBackground.copy(0.1f))
    ) {
        AsyncImage(
            model = category?.image,
            contentDescription = category?.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = category?.name ?: "",
            fontSize = 35.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomStart)
        )
    }
}

@Composable
private fun ProductListItem(
    modifier: Modifier = Modifier,
    product: ProductUI,
    index: Int,
    isGridLayout: Boolean,
    onToggleProductInWishlist: ((Int) -> Unit)? = null,
    onToggleProductInCart: ((Int) -> Unit)? = null,
    onRemove: ((Int) -> Unit)? = null,
    onProductClick: (Int) -> Unit,
) {
    ProductItem(
        modifier = modifier,
        product = product,
        isGrid = isGridLayout,
        imageWidth = 120.dp,
        onToggleInWishlist = if (onToggleProductInWishlist != null) {
            { onToggleProductInWishlist(index) }
        } else {
            null
        },
        onToggleInCart = if (onToggleProductInCart != null) {
            { onToggleProductInCart(index) }
        } else {
            null
        },
        onRemove = if (onRemove != null) {
            { onRemove(index) }
        } else {
            null
        },
        onClick = {
            onProductClick(index)
        }
    )
}

@Preview
@Composable
private fun ProductListPreview() {
    ShopyTheme {
        ProductList(
            products = products,
            isGridLayout = false,
            isLoading = false,
            onPaginate = {},
            onProductClick = {}
        )
    }
}


val products = listOf(
    ProductUI(
        productId = 1,
        title = "Product 1",
        description = "Product 1 description",
        image = "",
        price = 100,
        categoryName = "Category 1",
        isInWishList = false,
        isInCartList = false
    ),
    ProductUI(
        productId = 2,
        title = "Product 2",
        description = "Product 2 description",
        image = "",
        price = 230,
        categoryName = "Category 2",
        isInWishList = true,
        isInCartList = false
    ),
    ProductUI(
        productId = 1,
        title = "Product 1",
        description = "Product 1 description",
        image = "",
        price = 100,
        categoryName = "Category 1",
        isInWishList = false,
        isInCartList = false
    ),
    ProductUI(
        productId = 2,
        title = "Product 2",
        description = "Product 2 description",
        image = "",
        price = 230,
        categoryName = "Category 2",
        isInWishList = true,
        isInCartList = false
    ),
    ProductUI(
        productId = 1,
        title = "Product 1",
        description = "Product 1 description",
        image = "",
        price = 100,
        categoryName = "Category 1",
        isInWishList = false,
        isInCartList = false
    ),
    ProductUI(
        productId = 2,
        title = "Product 2",
        description = "Product 2 description",
        image = "",
        price = 230,
        categoryName = "Category 2",
        isInWishList = true,
        isInCartList = false
    ),
    ProductUI(
        productId = 1,
        title = "Product 1",
        description = "Product 1 description",
        image = "",
        price = 100,
        categoryName = "Category 1",
        isInWishList = false,
        isInCartList = false
    ),
    ProductUI(
        productId = 2,
        title = "Product 2",
        description = "Product 2 description",
        image = "",
        price = 230,
        categoryName = "Category 2",
        isInWishList = true,
        isInCartList = false
    ),
    ProductUI(
        productId = 1,
        title = "Product 1",
        description = "Product 1 description",
        image = "",
        price = 100,
        categoryName = "Category 1",
        isInWishList = false,
        isInCartList = false
    ),
    ProductUI(
        productId = 2,
        title = "Product 2",
        description = "Product 2 description",
        image = "",
        price = 230,
        categoryName = "Category 2",
        isInWishList = true,
        isInCartList = false
    ),
    ProductUI(
        productId = 1,
        title = "Product 1",
        description = "Product 1 description",
        image = "",
        price = 100,
        categoryName = "Category 1",
        isInWishList = false,
        isInCartList = false
    ),
    ProductUI(
        productId = 2,
        title = "Product 2",
        description = "Product 2 description",
        image = "",
        price = 230,
        categoryName = "Category 2",
        isInWishList = true,
        isInCartList = false
    ),
)