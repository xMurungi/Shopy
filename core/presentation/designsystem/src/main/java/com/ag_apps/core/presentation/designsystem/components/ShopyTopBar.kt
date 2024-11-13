package com.ag_apps.core.presentation.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ag_apps.core.presentation.designsystem.ShopyTheme

/**
 * @author Ahmed Guedmioui
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopyTopBar(
    modifier: Modifier = Modifier,
    titleText: String? = null,
    titleContent: (@Composable () -> Unit)? = null,
    navigationIcon: ImageVector? = null,
    navigationIconContent: (@Composable () -> Unit)? = null,
    navigationIconDescription: String? = null,
    actionIcon: ImageVector? = null,
    actionIconContent: (@Composable () -> Unit)? = null,
    actionIconDescription: String? = null,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    scrolledContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest
) {

    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        windowInsets = windowInsets,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = scrolledContainerColor
        ),
        title = {
            if (titleText != null) {
                Text(
                    text = titleText,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium
                )
            } else if (titleContent != null) {
                titleContent()
            }
        },
        navigationIcon = {
            if (navigationIcon != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = navigationIconDescription,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            } else if (navigationIconContent != null) {
                navigationIconContent()
            }
        },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = actionIconDescription,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(30.dp)
                    )
                }
            } else if (actionIconContent != null) {
                actionIconContent()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopyLargeTopBar(
    modifier: Modifier = Modifier,
    titleText: String,
    navigationIcon: ImageVector? = null,
    navigationIconDescription: String? = null,
    actionIcon: ImageVector? = null,
    actionIconDescription: String? = null,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.largeTopAppBarColors(
        containerColor = Color.Transparent, scrolledContainerColor = Color.Transparent
    ),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
) {

    LargeTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        windowInsets = windowInsets,
        colors = colors,
        title = {
            Text(
                text = titleText,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        navigationIcon = {
            if (navigationIcon != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = navigationIconDescription,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = actionIconDescription,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun ToolbarPreview() {
    ShopyTheme {
        ShopyTopBar(
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = Icons.Outlined.ArrowBackIosNew,
            actionIcon = Icons.Outlined.Search,
            titleText = "Shopy",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun LargeToolbarPreview() {
    ShopyTheme {
        ShopyLargeTopBar(
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = Icons.Outlined.ArrowBackIosNew,
            actionIcon = Icons.Outlined.Search,
            titleText = "Shopy",
        )
    }
}















