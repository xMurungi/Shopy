package com.ag_apps.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

/**
 * @author Ahmed Guedmioui
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopyScaffold(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState()),
    topBar: @Composable (TopAppBarScrollBehavior) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column(
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.surfaceContainerLow.copy(0.5f)
                )
            ) {
                topBar(scrollBehavior)
            }
        }
    ) { padding ->
        content(padding)
    }

}