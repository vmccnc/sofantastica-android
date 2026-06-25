package com.furniture.duet.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.furniture.duet.R
import com.furniture.duet.data.data_store.CountStorage
import com.furniture.duet.ui.account.AccountRoute
import com.furniture.duet.ui.account.AccountScreen
import com.furniture.duet.ui.auth.LoginRoute
import com.furniture.duet.ui.auth.LoginScreen
import com.furniture.duet.ui.auth.RegisterScreen
import com.furniture.duet.ui.auth.ResetPasswordScreen
import com.furniture.duet.ui.catalog.CatalogRoute
import com.furniture.duet.ui.home.HomeScreen
import com.furniture.duet.ui.favorites.FavoritesRoute
import com.furniture.duet.ui.cart.CartRoute
import com.furniture.duet.ui.detail.FurnitureDetailRoute
import com.furniture.duet.ui.theme.FurnitureCardBackgroundColor
import com.furniture.duet.ui.theme.SelectedRouteBackgroundColor
import com.furniture.duet.ui.theme.SelectedRouteIconColor
import kotlinx.coroutines.flow.collectLatest

sealed class Screen(val route: String, val icon: @Composable () -> Unit) {
    object Home : Screen("home", { Icon(painterResource(R.drawable.i_home), contentDescription = null) })
    object Favorites : Screen("favorites", { Icon(painterResource(R.drawable.i_heart), contentDescription = null) })
    object Catalog : Screen("catalog", { Icon(painterResource(R.drawable.i_catalog), contentDescription = null) })
    object Cart : Screen("cart", {
        Box(contentAlignment = Alignment.TopEnd) {
            val margin_12 = dimensionResource(R.dimen.margin_12)
            val size_24 = dimensionResource(R.dimen.size_24)
            Icon(painterResource(R.drawable.i_basket), contentDescription = null)

            val viewModel: MainViewModel = hiltViewModel()
            if (viewModel.cartCount > 0) {
                Text(
                    text = viewModel.cartCount.toString(),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(margin_12)
                        .size(size_24)
                        .background(FurnitureCardBackgroundColor, CircleShape)
                )
            }
        }
    })
    object Account : Screen("account", { Icon(painterResource(R.drawable.i_user), contentDescription = null) })
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Favorites,
        Screen.Catalog,
        Screen.Cart,
        Screen.Account
    )
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = navigationBarPadding)
            .background(MaterialTheme.colorScheme.background),
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            Row(
                modifier = Modifier.background(Color.White)
                    .padding(vertical = 5.dp, horizontal = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { screen ->
                    val selected = currentRoute?.startsWith(screen.route) ?: false
                    NavigationBarItem(
                        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = SelectedRouteBackgroundColor,
                            selectedIconColor = SelectedRouteIconColor
                        ),
                        icon = { screen.icon() }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Favorites.route) {
                FavoritesRoute(
                    onItemClick = { id -> navController.navigate("detail/$id") },
                    goToCatalog = { navController.navigate(Screen.Catalog.route) }
                )
            }
            composable(Screen.Catalog.route) {
                CatalogRoute(onItemClick = { id -> navController.navigate("detail/$id") })
            }
            composable(Screen.Cart.route) {
                CartRoute(
                    goToOrderHistory = { navController.navigate(Screen.Account.route) }
                )
            }
            composable(Screen.Account.route) {
                AccountRoute()
            }
            composable(
                route = "detail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {
                it.arguments?.getInt("id")?.let {
                    FurnitureDetailRoute(it)
                }
            }
        }
    }
}
