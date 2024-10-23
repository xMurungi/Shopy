package com.ag_apps.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ag_apps.core.presentation.designsystem.R
import com.ag_apps.core.presentation.designsystem.components.ShopyTopBar

/**
 * @author Ahmed Guedmioui
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScaffold(
    modifier: Modifier = Modifier,
    appName: String,
    isFilterOpen: Boolean,
    minPriceState: TextFieldState,
    maxPriceState: TextFieldState,
    onSearch: () -> Unit,
    toggleFilter: () -> Unit,
    toggleProductsLayout: () -> Unit,
    applyFilter: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerLow.copy(0.5f))
            ) {
                ShopyTopBar(
                    scrollBehavior = scrollBehavior,
                    title = appName,
                    actionIcon = Icons.Rounded.Search,
                    onActionClick = onSearch,
                    actionIconDescription = stringResource(R.string.search_for_products),
                )
                ProductsFilter(
                    isFilterOpen = isFilterOpen,
                    minPriceState = minPriceState,
                    maxPriceState = maxPriceState,
                    toggleFilter = toggleFilter,
                    toggleProductsLayout = toggleProductsLayout,
                    applyFilter = applyFilter,
                )

                Spacer(Modifier.height(8.dp))
                HorizontalDivider(Modifier.alpha(0.6f))
            }
        }
    ) { padding ->
        content(padding)
    }
}