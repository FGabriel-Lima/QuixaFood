package com.example.quixafood.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quixafood.models.mockItens
import com.example.quixafood.ui.components.BottomNavigationBar
import com.example.quixafood.ui.screens.DetailsScreen
import com.example.quixafood.ui.screens.FavoritesScreen
import com.example.quixafood.ui.screens.HelpScreen
import com.example.quixafood.ui.screens.HomeScreen
import com.example.quixafood.ui.screens.SearchScreen
import com.example.quixafood.ui.screens.SettingsScreen
import com.example.quixafood.ui.theme.QuixaFoodTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost

sealed class BottomBarScreen(val route: String, val icon: @Composable () -> Unit, val label: String) {
    object Home : BottomBarScreen(
        route = "home",
        icon = { androidx.compose.material3.Icon(Icons.Default.Home, contentDescription = "Tela Inicial") },
        label = "Tela Inicial"
    )
    object Favorites : BottomBarScreen(
        route = "favorites",
        icon = { androidx.compose.material3.Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
        label = "Favoritos"
    )
    object Help : BottomBarScreen(
        route = "help",
        icon = { androidx.compose.material3.Icon(Icons.Default.Info, contentDescription = "Ajuda") },
        label = "Ajuda"
    )
    object Search : BottomBarScreen(
        route = "search",
        icon = { androidx.compose.material3.Icon(Icons.Default.Search, contentDescription = "Buscar") },
        label = "Buscar"
    )
    object Settings : BottomBarScreen( // Tela de Configurações
        route = "settings",
        icon = { androidx.compose.material3.Icon(Icons.Default.Settings, contentDescription = "Configurações") },
        label = "Configurações"
    )
}

private fun navigateTo(
    navController: NavController,
    route: String,
    inclusive: Boolean = true,
    launchSingleTop: Boolean = true,
    restoreState: Boolean = true
) {
    val startDestinationId = navController.graph.startDestinationId
    navController.navigate(route) {
        popUpTo(startDestinationId) { this.inclusive = inclusive }
        this.launchSingleTop = launchSingleTop
        this.restoreState = restoreState
    }
}


private fun logout(context: Context) {
    Toast.makeText(context, "Logout realizado com sucesso!", Toast.LENGTH_SHORT).show()
}

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun NavGraph() {
    val context = LocalContext.current
    val navController = rememberAnimatedNavController()
    val isDarkTheme = remember { mutableStateOf(false) }
    val isNotificationsEnabled = remember { mutableStateOf(false) }
    QuixaFoodTheme(darkTheme = isDarkTheme.value) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }
        ) { innerPadding ->
            AnimatedNavHost(
                navController = navController,
                startDestination = BottomBarScreen.Home.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                // Tela Home
                composable(route = BottomBarScreen.Home.route, enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
                    HomeScreen(
                        onHomeClick = {
                            navigateTo(navController, BottomBarScreen.Home.route)
                        },
                        onFavoritesClick = {
                            navigateTo(navController, BottomBarScreen.Favorites.route)
                        },
                        onSettingsClick = {
                            navigateTo(
                                navController,
                                BottomBarScreen.Settings.route,
                                restoreState = false
                            )
                        },
                        onHelpClick = {
                            navigateTo(
                                navController,
                                BottomBarScreen.Help.route,
                                restoreState = false
                            )
                        },
                        onLogoutClick = { context: Context ->
                            logout(context)
                        },
                        navController = navController,
                        navigateTo = ::navigateTo
                    )
                }

                // Tela de Favoritos
                composable(route = BottomBarScreen.Favorites.route, enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
                    FavoritesScreen(
                        onHomeClick = {
                            navigateTo(navController, BottomBarScreen.Home.route)
                        },
                        onFavoritesClick = {
                            navigateTo(navController, BottomBarScreen.Favorites.route)
                        },
                        onSettingsClick = {
                            navigateTo(
                                navController,
                                BottomBarScreen.Settings.route,
                                restoreState = false
                            )
                        },
                        onHelpClick = {
                            navigateTo(
                                navController,
                                BottomBarScreen.Help.route,
                                restoreState = false
                            )
                        },
                        onLogoutClick = { context: Context ->
                            logout(context)
                        },
                        navController,
                        ::navigateTo
                    )
                }

                // Tela de Ajuda
                composable(route = BottomBarScreen.Help.route, enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
                    HelpScreen(navController = navController)
                }

                // Tela de Busca
                composable(route = BottomBarScreen.Search.route, enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
                    SearchScreen(navController = navController)
                }

                // Tela de Configurações
                composable(route = BottomBarScreen.Settings.route, enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) {
                    SettingsScreen(

                        onThemeToggle = { isDarkTheme.value = !isDarkTheme.value },
                        onNotificationsToggle = {
                            isNotificationsEnabled.value = !isNotificationsEnabled.value
                        }
                    )
                }

                // Tela de Detalhes
                composable(route = "details/{itemName}", enterTransition = { fadeIn() }, exitTransition = { fadeOut() }) { backStackEntry ->
                    val itemName = backStackEntry.arguments?.getString("itemName")
                    val selectedItem = mockItens.firstOrNull { it.name == itemName }
                    selectedItem?.let {
                        DetailsScreen(it)
                    } ?: run {
                        // Exiba uma mensagem de erro ou uma tela de fallback
                        Toast.makeText(context, "Item não encontrado!", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
}

