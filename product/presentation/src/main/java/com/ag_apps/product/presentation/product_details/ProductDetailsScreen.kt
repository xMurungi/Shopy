package com.ag_apps.product.presentation.product_details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ag_apps.core.presentation.RatingBar
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.domain.Product
import com.ag_apps.core.presentation.designsystem.components.ShopyScaffold
import com.ag_apps.core.presentation.designsystem.components.ShopyTopBar
import com.ag_apps.core.presentation.util.originalPrice
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
    productId: Int,
    onBack: (Boolean) -> Unit
) {

    LaunchedEffect(true) {
        viewModel.onAction(ProductDetailsAction.LoadProduct(productId))
    }

    BackHandler {
        onBack(viewModel.state.isProductUpdate)
    }

    ProductDetailsScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                ProductDetailsAction.GoBack -> onBack(viewModel.state.isProductUpdate)

                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailsScreen(
    state: ProductDetailsState,
    onAction: (ProductDetailsAction) -> Unit
) {

    ShopyScaffold(
        modifier = Modifier.fillMaxSize(),
        topBarContainerColor = Color.Transparent,
        topBar = {
            ShopyTopBar(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
                windowInsets = WindowInsets(
                    top = TopAppBarDefaults.TopAppBarExpandedHeight - 20.dp
                ),
                navigationIconContent = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clip(CircleShape)
                            .clickable { onAction(ProductDetailsAction.GoBack) }
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.surfaceContainerLowest.copy(0.8f))
                            .padding(9.dp)
                            .padding(end = 3.dp)

                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (state.product != null) {
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
                            imageVector = if (state.product.isInCartList) {
                                Icons.Rounded.ShoppingBag
                            } else {
                                Icons.Outlined.ShoppingBag
                            },
                            contentDescription = if (state.product.isInCartList) {
                                stringResource(R.string.remove_from_cart)
                            } else {
                                stringResource(R.string.add_to_cart)
                            },
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(
                            text = if (state.product.isInCartList) {
                                stringResource(R.string.remove_from_cart)
                            } else {
                                stringResource(R.string.add_to_cart)
                            },
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading && !state.isError && state.product == null) {
                CircularProgressIndicator()
            }
            if (state.isError && state.product == null) {
                Text(
                    text = stringResource(R.string.can_t_load_product_right_now),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }

        state.product?.let { product ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
            ) {

                ImagePager(
                    product = product,
                    onAction = onAction
                )

                Spacer(Modifier.height(18.dp))

                ScreenContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 150.dp),
                    product = product,
                    state = state,
                    onAction = onAction
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    product: Product,
    state: ProductDetailsState,
    onAction: (ProductDetailsAction) -> Unit
) {

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
    ) {

        Text(
            text = product.title,
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(start = 2.dp)
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .width(130.dp)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest.copy(0.4f))
                    .clickable { showBottomSheet = true }
                    .padding(vertical = 10.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (state.selectedFilterIndex == null) {
                        product.filter
                    } else {
                        product.filterList[state.selectedFilterIndex]
                    },
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp
                )


                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$${product.price.originalPrice(product.discount)}".take(6),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                    fontSize = 20.sp,
                    textDecoration = TextDecoration.LineThrough
                )

                Text(
                    text = "$${product.price}",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 23.sp,
                )
            }

        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.brand,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 2.dp)
                )

                Text(
                    text = product.categoryName,
                    fontSize = 14.sp,
                    lineHeight = 1.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.7f),
                    modifier = Modifier.padding(start = 2.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RatingBar(
                        rating = (product.rating / 2),
                        size = 7f
                    )
                    Spacer(Modifier.width(1.dp))
                    Text(
                        text = "(${product.rating})",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.9f)
                    )
                }
            }

        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = product.description,
            fontSize = 16.sp
        )
    }

    if (showBottomSheet) {
        FiltersBottomSheet(
            product = product,
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false },
            onAction = onAction
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FiltersBottomSheet(
    modifier: Modifier = Modifier,
    product: Product,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onAction: (ProductDetailsAction) -> Unit
) {
    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.select) + " " + product.filter,
                fontWeight = FontWeight.Medium,
                fontSize = 19.sp
            )

            Spacer(Modifier.height(16.dp))

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                maxItemsInEachRow = 3
            ) {
                for (index in 0..product.filterList.size - 1) {
                    Row(
                        modifier = Modifier
                            .width(100.dp)
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerLowest.copy(0.4f))
                            .clickable {
                                onAction(ProductDetailsAction.SelectFilter(index))
                                onDismissRequest()
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = product.filterList[index],
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

            }

        }
    }
}

@Composable
fun ImagePager(
    product: Product,
    modifier: Modifier = Modifier,
    autoSwipeDelay: Long = 3000L,
    onAction: (ProductDetailsAction) -> Unit
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
            modifier = modifier
                .fillMaxWidth()
                .height(450.dp)
                .shadow(
                    elevation = 16.dp,
                    spotColor = MaterialTheme.colorScheme.primary.copy(0.5f),
                )
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

        Icon(
            imageVector = if (product.isInWishList) {
                Icons.Rounded.Favorite
            } else {
                Icons.Outlined.FavoriteBorder
            },
            contentDescription = if (product.isInWishList) {
                stringResource(R.string.remove_from_wishlist)
            } else {
                stringResource(R.string.add_to_wishlist)
            },
            tint = if (product.isInWishList) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onBackground
            },
            modifier = Modifier
                .padding(16.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape,
                    spotColor = MaterialTheme.colorScheme.onBackground,
                )
                .clip(CircleShape)
                .size(48.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest.copy(0.9f))
                .clickable { onAction(ProductDetailsAction.ToggleProductInWishlist) }
                .padding(7.dp)
                .padding(top = 2.dp)
                .align(Alignment.BottomEnd)
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
                .background(MaterialTheme.colorScheme.onBackground.copy(0.7f))
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