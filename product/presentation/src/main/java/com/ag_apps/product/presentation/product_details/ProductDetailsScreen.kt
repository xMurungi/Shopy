package com.ag_apps.product.presentation.product_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ag_apps.core.presentation.RatingBar
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.domain.Product
import com.ag_apps.core.presentation.util.previewProducts

import com.ag_apps.product.presentation.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import org.koin.androidx.compose.koinViewModel

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun ProductDetailsScreenCore(
    viewModel: ProductDetailsViewModel = koinViewModel(),
    productId: Int
) {

    LaunchedEffect(true) {
        viewModel.onAction(ProductDetailsAction.LoadProduct(productId))
    }

    ProductDetailsScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailsScreen(
    state: ProductDetailsState,
    onAction: (ProductDetailsAction) -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                ),
                title = {},
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    onAction(ProductDetailsAction.ToggleProductInCart)
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Icon(
                        imageVector = if (state.product?.isInCartList == true) {
                            Icons.Rounded.ShoppingBag
                        } else {
                            Icons.Outlined.ShoppingBag
                        },
                        contentDescription = if (state.product?.isInCartList == true) {
                            stringResource(R.string.remove_from_cart)
                        } else {
                            stringResource(R.string.add_to_cart)
                        },
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = if (state.product?.isInCartList == true) {
                            stringResource(R.string.remove_from_cart)
                        } else {
                            stringResource(R.string.add_to_cart)
                        },
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    ) { padding ->
        state.product?.let { product ->
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                PreviewPage(product = product)

                Spacer(Modifier.height(12.dp))

                ScreenContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    product = product,
                    state = state,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    product: Product,
    state: ProductDetailsState,
    onAction: (ProductDetailsAction) -> Unit
) {

    Column(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    .padding(vertical = 10.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.filter,
                )

                Spacer(Modifier.width(38.dp))

                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            Icon(
                imageVector = if (state.product?.isInCartList == true) {
                    Icons.Rounded.Favorite
                } else {
                    Icons.Outlined.FavoriteBorder
                },
                contentDescription = if (state.product?.isInWishList == true) {
                    stringResource(R.string.remove_from_wishlist)
                } else {
                    stringResource(R.string.add_to_cart)
                },
                tint  = if (state.product?.isInWishList == true) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        shape = CircleShape,
                        spotColor = MaterialTheme.colorScheme.onBackground,
                    )
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(8.dp)
                    .padding(top = 1.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = product.brand,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 19.sp,
                    modifier = Modifier.padding(start = 2.dp)
                )

                Text(
                    text = product.categoryName,
                    fontSize = 12.sp,
                    lineHeight = 1.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.7f),
                    modifier = Modifier.padding(start = 2.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RatingBar(
                        rating = (product.rating / 2),
                        size = 6
                    )
                    Spacer(Modifier.width(1.dp))
                    Text(
                        text = "(${product.rating})",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                    )
                }
            }

            Text(
                text = "$${product.price}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
            )

        }


        Spacer(Modifier.height(16.dp))

        Text(
            text = product.description,
            fontSize = 15.sp
        )
    }
}

@Composable
fun ImagePager(
    product: Product,
    modifier: Modifier = Modifier,
    autoSwipeDelay: Long = 3000L
) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { product.images.size }
    )

    LaunchedEffect(pagerState) {
        while (true) {
            yield()
            delay(autoSwipeDelay)
            val nextPage = (pagerState.currentPage + 1) % product.images.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box {
        HorizontalPager(
            state = pagerState,
            modifier = modifier.fillMaxWidth()
        ) { page ->
            AsyncImage(
                model = product.images[page],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .background(MaterialTheme.colorScheme.onBackground.copy(0.1f))
            )
        }

        Text(
            text = product.title,
            fontSize = 17.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(0.7f)
                        ),
                    )
                )
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp, top = 18.dp)
                .align(Alignment.BottomStart)
        )
    }
}

@Composable
fun PreviewPage(
    product: Product
) {
    Box {
        Image(
            painter = painterResource(id = com.ag_apps.core.presentation.designsystem.R.drawable.man),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        )

        Text(
            text = product.title,
            fontSize = 17.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(0.8f)
                        ),
                    )
                )
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp, top = 22.dp)
                .align(Alignment.BottomStart)
        )
    }
}

@Preview
@Composable
private fun ProductDetailsScreenPreview() {
    ShopyTheme {
        ProductDetailsScreen(
            state = ProductDetailsState(
                product = previewProducts[0]
            ),
            onAction = {}
        )
    }
}