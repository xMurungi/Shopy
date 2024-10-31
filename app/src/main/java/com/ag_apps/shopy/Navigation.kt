package com.ag_apps.shopy

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ag_apps.auth.presentation.login.LoginScreenCore
import com.ag_apps.auth.presentation.register.RegisterScreenCore
import com.ag_apps.cart.presentation.CartScreenCore
import com.ag_apps.category.presentation.category.CategoryScreenCore
import com.ag_apps.category.presentation.category_overview.CategoryOverviewScreenCore
import com.ag_apps.checkout.presentation.checkout.CheckoutScreenCore
import com.ag_apps.checkout.presentation.success.SuccessScreen
import com.ag_apps.core.presentation.designsystem.components.ShopyBottomBar
import com.ag_apps.core.presentation.designsystem.components.bottomBarItems
import com.ag_apps.order.presentation.order_overview.OrderOverviewScreenCore
import com.ag_apps.product.presentation.product.ProductScreenCore
import com.ag_apps.product.presentation.product_overview.ProductOverviewScreenCore
import com.ag_apps.profile.presentation.ProfileScreenCore
import com.ag_apps.search.presentation.SearchScreenCore
import com.ag_apps.wishlist.presentation.WishlistScreenCore

/**
 * @author Ahmed Guedmioui
 */


@Composable
fun Navigation(
    isLoggedIn: Boolean,
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Main else Screen.Login,
    ) {

        // auth ------------------------------------------------------------------------------
        composable<Screen.Register> {
            RegisterScreenCore(
                onSuccessfulRegistration = {
                    navController.popBackStack()
                    navController.navigate(Screen.Main)
                },
                onLoginClick = {
                    navController.popBackStack()
                    navController.navigate(Screen.Login)
                }
            )
        }

        composable<Screen.Login> {
            LoginScreenCore(
                onLoginSuccess = {
                    navController.popBackStack()
                    navController.navigate(Screen.Main)
                },
                inSignUpClick = {
                    navController.popBackStack()
                    navController.navigate(Screen.Register)
                }
            )
        }

        // main ------------------------------------------------------------------------------
        composable<Screen.Main> {
            MainBottomBar(navController = navController)
        }

        // product ------------------------------------------------------------------------------
        composable<Screen.Product> { backStackEntry ->

            val product: Screen.Product = backStackEntry.toRoute()
            val productId = product.productId

            ProductScreenCore(
                productId = productId,
                onBack = { isProductUpdate ->
                    navController.popBackStack()
                    if (isProductUpdate) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "updatedProductId",
                            productId
                        )
                    }
                }
            )
        }

        // search ------------------------------------------------------------------------------
        composable<Screen.Search> {
            val updatedProductId =
                navController.currentBackStackEntry?.savedStateHandle?.get<Int>("updatedProductId")

            SearchScreenCore(
                updatedProductId = updatedProductId,
                onProductClick = { productId ->
                    navController.navigate(Screen.Product(productId))
                }
            )
        }

        // category ------------------------------------------------------------------------------
        composable<Screen.CategoryDetails> { backStackEntry ->
            val categoryDetails: Screen.CategoryDetails = backStackEntry.toRoute()
            val categoryId = categoryDetails.categoryId

            CategoryScreenCore(
                categoryId = categoryId,
                onProductClick = { productId ->
                    navController.navigate(Screen.Product(productId))
                },
                onSearch = {
                    navController.navigate(Screen.Search)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // checkout ------------------------------------------------------------------------------
        composable<Screen.Checkout> {
            CheckoutScreenCore(
                onOrdersSubmitted = {
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate(Screen.Success)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.Success> {
            SuccessScreen(
                onContinue = {
                    navController.popBackStack()
                    navController.navigate(Screen.OrderDetails(0))
                }
            )
        }

        // order ------------------------------------------------------------------------------
        composable<Screen.OrderOverview> {
            OrderOverviewScreenCore(
                onOrderClick = { orderId ->
                    navController.navigate(Screen.OrderDetails(orderId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.OrderDetails> { backStackEntry ->

            val orderDetails: Screen.OrderDetails = backStackEntry.toRoute()

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "order ${orderDetails.orderId}")
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
        contentWindowInsets = WindowInsets(top = 0.dp),
        bottomBar = {
            ShopyBottomBar(
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
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { -it } },
            popEnterTransition = { slideInHorizontally { -it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {

            // product ----------------------------------------------------------------------------
            composable<BottomBarScreen.ProductOverview> {
                ProductOverviewScreenCore(
                    appName = stringResource(R.string.app_name),
                    onProductClick = { productId ->
                        navController.navigate(Screen.Product(productId))
                    },
                    onCategoryClick = { categoryId ->
                        navController.navigate(Screen.CategoryDetails(categoryId))
                    },
                    onSearch = {
                        navController.navigate(Screen.Search)
                    }
                )
            }

            // category ---------------------------------------------------------------------------
            composable<BottomBarScreen.CategoryOverview> {
                CategoryOverviewScreenCore(
                    onCategoryClick = { categoryId ->
                        navController.navigate(Screen.CategoryDetails(categoryId))
                    },
                    onSearch = {
                        navController.navigate(Screen.Search)
                    }
                )
            }

            // cart ------------------------------------------------------------------------------
            composable<BottomBarScreen.Cart> {
                CartScreenCore(
                    onProductClick = { productId ->
                        navController.navigate(Screen.Product(productId))
                    },
                    onCheckout = {
                        navController.navigate(Screen.Checkout)
                    }
                )
            }

            // wishlist ---------------------------------------------------------------------------
            composable<BottomBarScreen.Wishlist> {
                WishlistScreenCore(
                    onProductClick = { productId ->
                        navController.navigate(Screen.Product(productId))
                    },
                    onSearch = {
                        navController.navigate(Screen.Search)
                    }
                )
            }

            // profile ----------------------------------------------------------------------------
            composable<BottomBarScreen.Profile> {
                ProfileScreenCore(
                    onOrdersClick = {
                        navController.navigate(Screen.OrderOverview)
                    },
                    onLogoutClick = {
                        navController.popBackStack()
                        navController.navigate(Screen.Login)
                    }
                )
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
    data class Product(val productId: Int) : Screen

    @kotlinx.serialization.Serializable
    data class CategoryDetails(val categoryId: Int) : Screen

    @kotlinx.serialization.Serializable
    data object Search : Screen

    @kotlinx.serialization.Serializable
    data object Checkout : Screen

    @kotlinx.serialization.Serializable
    data object Success : Screen

    @kotlinx.serialization.Serializable
    data object OrderOverview : Screen

    @kotlinx.serialization.Serializable
    data class OrderDetails(val orderId: Int) : Screen
}


















