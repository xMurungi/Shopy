package com.ag_apps.core.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            val totalItems = if (isGridLayout){
                gridState.layoutInfo.totalItemsCount
            }
            else {
                listState.layoutInfo.totalItemsCount
            }

            val lastVisibleIndex = if (isGridLayout) {
                gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            }
            else {
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
            modifier = modifier.padding(horizontal = 6.dp),
            columns = GridCells.Fixed(2),
            state = gridState
        ) {
            itemsIndexed(products) { index, product ->
                ProductListItem(
                    modifier = Modifier
                        .padding(6.dp)
                        .padding(top = if (index == 0 || index == 1) 8.dp else 0.dp),
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
            modifier = modifier.padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            state = listState
        ) {
            itemsIndexed(
                items = products,
                key = { _, article -> article.productId }
            ) { index, product ->
                ProductListItem(
                    modifier = Modifier.height(120.dp),
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