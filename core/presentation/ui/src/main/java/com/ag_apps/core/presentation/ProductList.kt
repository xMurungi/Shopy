package com.ag_apps.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.ag_apps.core.presentation.model.previewProducts
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.yield

/**
 * @author Ahmed Guedmioui
 */
@Composable
fun ProductList(
    modifier: Modifier = Modifier,
    products: List<ProductUI>,
    isGridLayout: Boolean,
    isLoading: Boolean,
    isApplyingFilter: Boolean,
    categories: List<Category> = emptyList(),
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
            if (categories.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    CategoryPager(categories = categories)
                }
            }

            if (products.isNotEmpty() && !isApplyingFilter) {
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
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(bottom = 8.dp),
            state = listState
        ) {
            if (categories.isNotEmpty()) {
                item {
                    CategoryPager(categories = categories)
                    Spacer(Modifier.height(12.dp))
                }
            }
            if (products.isNotEmpty() && !isApplyingFilter) {
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
}

@Composable
fun CategoryPager(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    autoSwipeDelay: Long = 3000L
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { categories.size }
    )

    LaunchedEffect(pagerState) {
        while (true) {
            yield()
            delay(autoSwipeDelay)
            val nextPage = (pagerState.currentPage + 1) % categories.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) { page ->
        CategoryListItem(
            category = categories[page],
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun CategoryListItem(
    modifier: Modifier = Modifier,
    category: Category,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.onBackground.copy(0.1f))
    ) {
        AsyncImage(
            model = category.image,
            contentDescription = category.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = category.name,
            fontSize = 35.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier
                .offset(
                    x = 2.dp,
                    y = 2.dp
                )
                .alpha(0.75f)
                .padding(8.dp)
                .align(Alignment.BottomStart)
        )

        Text(
            text = category.name,
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
            products = previewProducts,
            isGridLayout = false,
            isLoading = false,
            isApplyingFilter = false,
            onPaginate = {},
            onProductClick = {},
        )
    }
}

