package com.ag_apps.category.presentation.category_overview

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ag_apps.category.presentation.R
import com.ag_apps.core.domain.models.Category
import com.ag_apps.core.presentation.designsystem.ShopyTheme
import com.ag_apps.core.presentation.designsystem.components.ShopyScaffold
import com.ag_apps.core.presentation.designsystem.components.ShopyTopBar
import org.koin.androidx.compose.koinViewModel

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun CategoryOverviewScreenCore(
    viewModel: CategoryOverviewViewModel = koinViewModel(),
    onCategoryClick: (Int) -> Unit,
    onSearch: () -> Unit,
) {
    CategoryOverviewScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is CategoryOverviewAction.ClickCategory -> {
                    onCategoryClick(
                        viewModel.state.categories[action.categoryIndex].categoryId
                    )
                }

                is CategoryOverviewAction.Search -> {
                    onSearch()
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryOverviewScreen(
    state: CategoryOverviewState,
    onAction: (CategoryOverviewAction) -> Unit
) {

    ShopyScaffold(
        topBar = { scrollBehavior ->
            ShopyTopBar(
                scrollBehavior = scrollBehavior,
                titleText = stringResource(R.string.categories),
                actionIcon = Icons.Rounded.Search,
                actionIconDescription = stringResource(R.string.search_products),
                onActionClick = { onAction(CategoryOverviewAction.Search) },
            )
        },
        onRefresh = {
            onAction(CategoryOverviewAction.Refresh)
        }
    ) { padding ->

        if (state.categories.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
            ) {
                itemsIndexed(state.categories) { index, category ->
                    CategoryItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onAction(CategoryOverviewAction.ClickCategory(index))
                            },
                        category = category
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading && !state.isError && state.categories.isEmpty()) {
                    CircularProgressIndicator()
                }
                if (state.isError && state.categories.isEmpty()) {
                    Text(
                        text = stringResource(R.string.can_t_load_categories_right_now),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                }
                if (!state.isLoading && !state.isError && state.categories.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_categories_found),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.7f),
                        textAlign = TextAlign.Center,
                    )
                }
            }

        }

    }

}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
) {
    Box(
        modifier = modifier
            .height(200.dp)
    ) {

        AsyncImage(
            model = category.image,
            contentScale = ContentScale.Crop,
            contentDescription = category.name,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black,
                        )
                    )
                )
                .padding(horizontal = 22.dp)
                .padding(top = 20.dp, bottom = 16.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = category.name,
                maxLines = 1,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp,
            )

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .alpha(0.7f)
            )
        }
    }
}

@Preview
@Composable
private fun CategoryOverviewScreenPreview() {
    ShopyTheme {
        CategoryOverviewScreen(
            state = CategoryOverviewState(),
            onAction = {}
        )
    }
}