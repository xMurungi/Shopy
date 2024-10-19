package com.ag_apps.core.presentation

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.core.domain.Product
import com.ag_apps.core.presentation.designsystem.ShopyTheme

/**
 * @author Ahmed Guedmioui
 */
@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    isGrid: Boolean,
    imageWidth: Dp = 100.dp,
    onAddToWishlist: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
    onAddToCart: (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    if (isGrid && onRemove == null) {
        GridProductItem(
            modifier = modifier,
            product = product,
            onAddToWishlist = onAddToWishlist,
            onAddToCart = onAddToCart,
            onClick = onClick
        )
    } else {
        ColumnProductItem(
            modifier = modifier,
            product = product,
            imageWidth = imageWidth,
            onAddToWishlist = onAddToWishlist,
            onRemove = onRemove,
            onAddToCart = onAddToCart,
            onClick = onClick
        )
    }
}

@Composable
fun GridProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    onAddToWishlist: (() -> Unit)? = null,
    onAddToCart: (() -> Unit)? = null,
    onClick: () -> Unit,
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
        ) {
            Image(
                painter = painterResource(com.ag_apps.core.presentation.designsystem.R.drawable.man),
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(bottom = 23.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(
                        RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
                    )
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            )

            ProductActionsSection(
                product = product,
                onAddToWishlist = onAddToWishlist,
                onAddToCart = onAddToCart,
                modifier = Modifier
                    .padding(horizontal = 7.dp)
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
                    .padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(2.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp)
        ) {

            Text(
                text = product.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 1.sp
            )

            Text(
                text = "$${product.price}",
                fontSize = 15.sp,
                lineHeight = 1.sp
            )
        }
    }
}


@Composable
fun ColumnProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    onAddToWishlist: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
    onAddToCart: (() -> Unit)? = null,
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
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .clickable { onClick() }
                    .padding(end = 7.dp)
            ) {

                Image(
                    painter = painterResource(com.ag_apps.core.presentation.designsystem.R.drawable.man),
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(imageWidth)
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 16.dp, topStart = 16.dp
                            )
                        )
                        .background(MaterialTheme.colorScheme.surfaceContainer)
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
            onAddToWishlist = onAddToWishlist,
            onAddToCart = onAddToCart,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun ProductActionsSection(
    modifier: Modifier = Modifier,
    product: Product,
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
                        shape = CircleShape
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
                        Color.Red
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                    modifier = Modifier
                        .clickable { onAddToCart() }
                        .padding(6.dp)
                        .padding(bottom = 1.dp)
                        .fillMaxSize()
                        .alpha(0.6f)
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
                        shape = CircleShape
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
                        Color.Red
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                    modifier = Modifier
                        .clickable { onAddToWishlist() }
                        .padding(6.dp)
                        .padding(top = 0.7.dp)
                        .fillMaxSize()
                        .alpha(0.6f)
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
            product = Product(
                categoryName = "Category",
                title = "Product Title Product",
                description = "Product Description",
                images = listOf("https://picsum.photos/200"),
                price = 100,
                productId = 1,
                isInWishList = false,
                isInCartList = true
            ),
            isGrid = true,
            onClick = {},
            onAddToWishlist = {},
            onAddToCart = {},
        )
    }
}