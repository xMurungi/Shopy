package com.ag_apps.core.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ag_apps.core.domain.Product

/**
 * @author Ahmed Guedmioui
 */
@Composable
fun ProductList(
    modifier: Modifier = Modifier,
    isGridLayout: Boolean,
    products: List<Product>,
    onToggleProductInWishlist: ((Int) -> Unit)? = null,
    onToggleProductInCart: ((Int) -> Unit)? = null,
    onRemove: ((Int) -> Unit)? = null,
    onProductClick: (Int) -> Unit,
) {
    if (isGridLayout) {
        LazyVerticalGrid(
            modifier = modifier.padding(horizontal = 6.dp),
            columns = GridCells.Fixed(2),
        ) {
            itemsIndexed(products) { index, product ->
                Item(
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
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            itemsIndexed(products) { index, product ->
                Item(
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
private fun Item(
    modifier: Modifier = Modifier,
    product: Product,
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