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
    toggleProductInWishlist: (Int) -> Unit,
    toggleProductInCart: (Int) -> Unit,
    selectProduct: (Int) -> Unit,
) {
    if (isGridLayout) {
        LazyVerticalGrid(
            modifier = modifier.padding(horizontal = 6.dp),
            columns = GridCells.Fixed(2),
        ) {
            itemsIndexed(products) { index, product ->
                ProductItem(
                    modifier = Modifier
                        .padding(6.dp)
                        .padding(top = if (index == 0 || index == 1) 8.dp else 0.dp),
                    product = product,
                    isGrid = true,
                    onAddToWishlist = {
                        toggleProductInWishlist(index)
                    },
                    onAddToCart = {
                        toggleProductInCart(index)
                    },
                    onClick = {
                        selectProduct(index)
                    }
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            itemsIndexed(products) { index, product ->
                ProductItem(
                    modifier = Modifier.height(120.dp),
                    product = product,
                    isGrid = false,
                    imageWidth = 120.dp,
                    onAddToWishlist = {
                        toggleProductInWishlist(index)
                    },
                    onAddToCart = {
                        toggleProductInCart(index)
                    },
                    onClick = {
                        selectProduct(index)
                    }
                )

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}