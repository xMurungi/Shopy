package com.ag_apps.category.presentation.category_overview

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ag_apps.category.presentation.R
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
                navigationIcon = Icons.Rounded.ArrowBackIosNew,
                navigationIconDescription = stringResource(R.string.go_back),
                onActionClick = { onAction(CategoryOverviewAction.Search) },
            )

            Spacer(Modifier.height(8.dp))

            HorizontalDivider(Modifier.alpha(0.6f))
        }
    ) { padding ->

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