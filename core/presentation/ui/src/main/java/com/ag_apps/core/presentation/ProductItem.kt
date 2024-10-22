package com.ag_apps.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.model.ProductUI

/**
 * @author Ahmed Guedmioui
 */
@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: ProductUI,
    isGrid: Boolean,
    imageWidth: Dp = 100.dp,
    onToggleInWishlist: (() -> Unit)? = null,
    onToggleInCart: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    if (isGrid && onRemove == null) {
        GridProductItem(
            modifier = modifier,
            product = product,
            onToggleProductInWishlist = onToggleInWishlist,
            onToggleProductInCart = onToggleInCart,
            onClick = onClick
        )
    } else {
        ColumnProductItem(
            modifier = modifier,
            product = product,
            imageWidth = imageWidth,
            onToggleProductInWishlist = onToggleInWishlist,
            onToggleProductInCart = onToggleInCart,
            onRemove = onRemove,
            onClick = onClick
        )
    }
}

@Composable
fun GridProductItem(
    modifier: Modifier = Modifier,
    product: ProductUI,
    onToggleProductInWishlist: (() -> Unit)? = null,
    onToggleProductInCart: (() -> Unit)? = null,
    onClick: () -> Unit,
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(bottom = 23.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.onBackground.copy(0.1f))
            )

            ProductActionsSection(
                product = product,
                onAddToWishlist = onToggleProductInWishlist,
                onAddToCart = onToggleProductInCart,
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .padding(bottom = 8.dp)
                    .align(Alignment.BottomEnd)
            )

            Text(
                text = product.categoryName,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(0.7f),
                lineHeight = 1.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 10.dp)
            )
        }

        Spacer(Modifier.height(6.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 8.dp)
        ) {

            Text(
                text = product.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = "$${product.price}",
                fontSize = 15.sp,
            )
        }
    }
}


@Composable
fun ColumnProductItem(
    modifier: Modifier = Modifier,
    product: ProductUI,
    onToggleProductInWishlist: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
    onToggleProductInCart: (() -> Unit)? = null,
    onClick: () -> Unit,
    imageWidth: Dp = 100.dp,
) {
    Box(
        modifier = Modifier
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 10.dp)
        ) {
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .clickable { onClick() }
                    .padding(end = 7.dp)
            ) {

                AsyncImage(
                    model = product.image,
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(imageWidth)
                        .background(MaterialTheme.colorScheme.onBackground.copy(0.1f))
                )

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = product.categoryName,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.7f),
                            )

                            Text(
                                text = product.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 17.sp,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.onBackground,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        Text(
                            text = "$${product.price}",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    if (onRemove != null) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground.copy(0.4f),
                            modifier = Modifier
                                .padding(top = 7.dp)
                                .clickable { onRemove() }
                                .size(22.dp)
                                .align(Alignment.TopEnd)
                        )
                    }
                }
            }
        }

        ProductActionsSection(
            product = product,
            onAddToWishlist = onToggleProductInWishlist,
            onAddToCart = onToggleProductInCart,
            modifier = Modifier
                .padding(end = 18.dp)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun ProductActionsSection(
    modifier: Modifier = Modifier,
    product: ProductUI,
    onAddToWishlist: (() -> Unit)? = null,
    onAddToCart: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
    ) {

        if (onAddToCart != null) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = CircleShape,
                        spotColor = MaterialTheme.colorScheme.onBackground,
                    )
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (product.isInCartList) {
                        Icons.Rounded.ShoppingBag
                    } else {
                        Icons.Outlined.ShoppingBag
                    },
                    contentDescription = null,
                    tint = if (product.isInCartList) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onBackground.copy(0.6f)
                    },
                    modifier = Modifier
                        .clickable { onAddToCart() }
                        .padding(6.dp)
                        .padding(bottom = 1.dp)
                        .fillMaxSize()
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        if (onAddToWishlist != null) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = CircleShape,
                        spotColor = MaterialTheme.colorScheme.onBackground,
                    )
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = if (product.isInWishList) {
                        Icons.Rounded.Favorite
                    } else {
                        Icons.Rounded.FavoriteBorder
                    },
                    contentDescription = null,
                    tint = if (product.isInWishList) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onBackground.copy(0.6f)
                    },
                    modifier = Modifier
                        .clickable { onAddToWishlist() }
                        .padding(6.dp)
                        .padding(top = 0.7.dp)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProductItemPreview() {
    ShopyTheme {
        ProductItem(
            modifier = Modifier
                .width(220.dp),
            imageWidth = 120.dp,
            product = ProductUI(
                categoryName = "Category",
                title = "Product Title Product",
                description = "Product Description",
                image = "",
                price = 100,
                productId = 1,
                isInWishList = false,
                isInCartList = true
            ),
            isGrid = true,
            onClick = {},
            onToggleInWishlist = {},
            onToggleInCart = {},
        )
    }
}