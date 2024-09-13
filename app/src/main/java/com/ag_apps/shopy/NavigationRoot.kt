package com.ag_apps.shopy

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ag_apps.auth.presentation.login.LoginScreenCore
import com.ag_apps.core.presentation.designsystem.components.Background
import com.ag_apps.core.presentation.designsystem.components.Button
import com.ag_apps.core.presentation.designsystem.components.BottomBar
import com.ag_apps.core.presentation.designsystem.components.Scaffold
import com.ag_apps.core.presentation.designsystem.components.bottomBarItems

/**
 * @author Ahmed Guedmioui
 */

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean,
) {

    Background()

    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Main else Screen.Login,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it } },
        popEnterTransition = { slideInHorizontally { -it } },
        popExitTransition = { slideOutHorizontally { it } }
    ) {

        // auth ------------------------------------------------------------------------------
        composable<Screen.Register> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "register")
            }
        }

        composable<Screen.Login> {
            LoginScreenCore(
                onLoginSuccess = {
                    navController.navigate(Screen.Main)
                },
                inSignUpClick = {
                    navController.navigate(Screen.Register)
                }
            )
        }

        // main ------------------------------------------------------------------------------
        composable<Screen.Main> {
            MainBottomBar(navController = navController)
        }

        // product ------------------------------------------------------------------------------
        composable<Screen.ProductDetails> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "product details")
            }
        }

        // search ------------------------------------------------------------------------------
        composable<Screen.Search> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "search")
            }
        }

        // category ------------------------------------------------------------------------------
        composable<Screen.CategoryDetails> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "category details")
            }
        }

        // checkout ------------------------------------------------------------------------------
        composable<Screen.Checkout> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "checkout")
            }
        }

        composable<Screen.Success> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "success")
            }
        }

        // order ------------------------------------------------------------------------------
        composable<Screen.OrderOverview> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "order overview")
            }
        }

        composable<Screen.OrderDetails> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "order details")
            }
        }
    }
}

@Composable
private fun MainBottomBar(
    navController: NavHostController
) {
    val bottomBarNavController = rememberNavController()

    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        bottomAppBar = {
            BottomBar(
                items = bottomBarItems,
                selectedItem = selectedItem,
                onItemClick = { index ->

                    selectedItem = index

                    val navigateToScreen = when (index) {
                        0 -> BottomBarScreen.ProductOverview
                        1 -> BottomBarScreen.CategoryOverview
                        2 -> BottomBarScreen.Cart
                        3 -> BottomBarScreen.Wishlist
                        else -> BottomBarScreen.Profile
                    }

                    bottomBarNavController.navigate(navigateToScreen) {
                        popUpTo(bottomBarNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = bottomBarNavController,
            startDestination = BottomBarScreen.ProductOverview,
            enterTransition = { fadeIn(initialAlpha = 1f) },
            exitTransition = { fadeOut(targetAlpha = 0f) },
            popEnterTransition = { fadeIn(initialAlpha = 1f) },
            popExitTransition = { fadeOut(targetAlpha = 0f) }
        ) {

            // product ----------------------------------------------------------------------------
            composable<BottomBarScreen.ProductOverview> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "product overview")

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            navController.navigate(Screen.ProductDetails)
                        },
                        text = "go to product details"
                    )
                }
            }

            // category ---------------------------------------------------------------------------
            composable<BottomBarScreen.CategoryOverview> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "category overview")
                }
            }

            // cart ------------------------------------------------------------------------------
            composable<BottomBarScreen.Cart> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "cart")
                }
            }

            // wishlist ---------------------------------------------------------------------------
            composable<BottomBarScreen.Wishlist> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "wishlist")
                }
            }

            // profile ----------------------------------------------------------------------------
            composable<BottomBarScreen.Profile> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "profile")
                }
            }

        }
    }
}

sealed interface BottomBarScreen {
    @kotlinx.serialization.Serializable
    data object ProductOverview : Screen

    @kotlinx.serialization.Serializable
    data object CategoryOverview : Screen

    @kotlinx.serialization.Serializable
    data object Cart : Screen

    @kotlinx.serialization.Serializable
    data object Wishlist : Screen

    @kotlinx.serialization.Serializable
    data object Profile : Screen
}

sealed interface Screen {
    @kotlinx.serialization.Serializable
    data object Register : Screen

    @kotlinx.serialization.Serializable
    data object Login : Screen

    @kotlinx.serialization.Serializable
    data object Main : Screen

    @kotlinx.serialization.Serializable
    data object ProductDetails : Screen

    @kotlinx.serialization.Serializable
    data object CategoryDetails : Screen

    @kotlinx.serialization.Serializable
    data object Search : Screen

    @kotlinx.serialization.Serializable
    data object Checkout : Screen

    @kotlinx.serialization.Serializable
    data object Success : Screen

    @kotlinx.serialization.Serializable
    data object OrderOverview : Screen

    @kotlinx.serialization.Serializable
    data object OrderDetails : Screen
}


















