package pl.sofantastica.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import pl.sofantastica.R
import pl.sofantastica.ui.account.AccountScreen
import pl.sofantastica.ui.catalog.CatalogRoute
import pl.sofantastica.ui.home.HomeScreen
import pl.sofantastica.ui.favorites.FavoritesRoute
import pl.sofantastica.ui.cart.CartRoute
import pl.sofantastica.ui.detail.FurnitureDetailRoute

sealed class Screen(val route: String, val label: String, val icon: @Composable () -> Unit) {
    object Home : Screen("home", "Home", { Icon(Icons.Default.Home, contentDescription = null) })
    object Favorites : Screen("favorites", "Favorites", { Icon(Icons.Default.Favorite, contentDescription = null) })
    object Catalog : Screen("catalog", "Catalog", { Icon(Icons.Default.List, contentDescription = null) })
    object Cart : Screen("cart", "Cart", { Icon(Icons.Default.ShoppingCart, contentDescription = null) })
    object Account : Screen("account", "Account", { Icon(Icons.Default.AccountCircle, contentDescription = null) })
}

@Composable
fun MainScreen(goToLogInPage: () -> Unit) {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route
            val showBottom = currentRoute?.startsWith("detail") != true
            if (showBottom) {
                NavigationBar {
                    val items = listOf(Screen.Home, Screen.Favorites, Screen.Catalog, Screen.Cart, Screen.Account)
                    items.forEach { screen ->
                        val selected = currentRoute == screen.route
                        val iconModifier = Modifier.size(24.dp)
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Box(modifier = iconModifier) { screen.icon() } },
                            label = { Text(screen.label, style = MaterialTheme.typography.bodySmall) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Image(painter = painterResource(R.drawable.background_image),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Favorites.route) {
                FavoritesRoute(
                    onItemClick = { id -> navController.navigate("detail/$id") },
                    goToCatalog = { navController.navigate(Screen.Catalog.route) }
                )
            }
            composable(Screen.Catalog.route) {
                CatalogRoute(onItemClick = { id -> navController.navigate("detail/$id") })
            }
            composable(Screen.Cart.route) { CartRoute("demoUser",
                goToCatalog = { navController.navigate(Screen.Catalog.route) }) }
            composable(Screen.Account.route) { AccountScreen(goToLogInPage) }

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
