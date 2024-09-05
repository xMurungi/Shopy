package com.ag_apps.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ag_apps.core.presentation.designsystem.Poppins
import com.ag_apps.core.presentation.designsystem.ShopyTheme

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    items: List<BottomNavigationItem> = bottomBarItems,
    selectedItem: Int,
    onItemClick: (Int) -> Unit,
) {

    NavigationBar(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp)
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedItem == index
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        onItemClick(index)
                    },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) {
                                item.selectedIcon
                            } else {
                                item.unselectedIcon
                            },
                            contentDescription = item.label,
                            tint = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onBackground.copy(0.5f)
                            },
                            modifier = Modifier.size(27.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontFamily = Poppins,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            },
                            fontWeight = if (isSelected) {
                                FontWeight.Medium
                            } else {
                                FontWeight.Thin
                            },
                            fontSize = 14.sp
                        )
                    }
                )
            }
        }
    }
}

data class BottomNavigationItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Preview(showBackground = true)
@Composable
private fun BottomPreview() {
    ShopyTheme {
        BottomBar(
            items = bottomBarItems,
            selectedItem = 0,
            onItemClick = { index -> }
        )
    }
}

val bottomBarItems = listOf(
    BottomNavigationItem(
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavigationItem(
        label = "Category",
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart
    ),
    BottomNavigationItem(
        label = "Cart",
        selectedIcon = Icons.Filled.ShoppingBag,
        unselectedIcon = Icons.Outlined.ShoppingBag
    ),
    BottomNavigationItem(
        label = "Wishlist",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    ),
    BottomNavigationItem(
        label = "Profile",
        selectedIcon = Icons.Filled.Person2,
        unselectedIcon = Icons.Outlined.Person2
    )
)