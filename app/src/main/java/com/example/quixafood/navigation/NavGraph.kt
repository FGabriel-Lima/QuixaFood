package com.example.quixafood.navigation

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quixafood.data.AuthRepository
import com.example.quixafood.models.mockItens
import com.example.quixafood.ui.components.BottomNavigationBar
import com.example.quixafood.ui.screens.DetailsScreen
import com.example.quixafood.ui.screens.FavoritesScreen
import com.example.quixafood.ui.screens.HelpScreen
import com.example.quixafood.ui.screens.HomeScreen
import com.example.quixafood.ui.screens.LoginScreen
import com.example.quixafood.ui.screens.RegisterScreen
import com.example.quixafood.ui.screens.ResetPasswordScreen
import com.example.quixafood.ui.screens.SearchScreen
import com.example.quixafood.ui.screens.SettingsScreen
import com.example.quixafood.ui.theme.QuixaFoodTheme
import com.example.quixafood.viewmodel.AuthViewModel
import java.time.LocalTime

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
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) { this.inclusive = inclusive }
        this.launchSingleTop = launchSingleTop
        this.restoreState = restoreState
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun isNightMode(): Boolean{
    val now = LocalTime.now()
    return now.hour in 19..23 || now.hour in 0..5
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterial3Api
@Composable
fun NavGraph(context: Context, useAnimation: Boolean = true) {
    val navController = rememberNavController()
    val isDarkTheme = remember { mutableStateOf(isNightMode()) }
    val isNotificationsEnabled = remember { mutableStateOf(false) }
    val authViewModel = AuthViewModel(AuthRepository())

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    val noBottomBarScreens = listOf("login", "register", "resetPassword")

    QuixaFoodTheme(darkTheme = isDarkTheme.value) {
        Scaffold(
                bottomBar = {
                    if (currentDestination !in noBottomBarScreens) {
                        BottomNavigationBar(navController = navController)
                    }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination =
                if(authViewModel.isuserlogged()) {
                    BottomBarScreen.Home.route
                } else {
                    "login"
                },
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("login") {
                    LoginScreen(navController, authViewModel, context)
                }

                composable("register") {
                    RegisterScreen(navController, authViewModel, context)
                }

                composable("resetPassword") {
                    ResetPasswordScreen(navController, authViewModel, context)
                }

                // Tela Home
                composable(BottomBarScreen.Home.route) {
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
                        onLogoutClick = {
                            authViewModel.logout()
                            navigateTo(navController, "login", restoreState = false)
                        },
                        navController = navController,
                        navigateTo = ::navigateTo
                    )
                }

                // Tela de Favoritos
                composable(BottomBarScreen.Favorites.route) {
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
                        onLogoutClick = {
                            authViewModel.logout()
                            navigateTo(navController, "login", restoreState = false)
                        },
                        navController,
                        ::navigateTo
                    )
                }

                // Tela de Ajuda
                composable(BottomBarScreen.Help.route) {
                    HelpScreen(navController = navController)
                }

                // Tela de Busca
                composable(BottomBarScreen.Search.route) {
                    SearchScreen(navController = navController)
                }

                // Tela de Configurações
                composable(BottomBarScreen.Settings.route) {
                    SettingsScreen(
                        onThemeToggle = { isDarkTheme.value = !isDarkTheme.value },
                        onNotificationsToggle = {
                            isNotificationsEnabled.value = !isNotificationsEnabled.value
                        }
                    )
                }

                // Tela de Detalhes
                composable("details/{itemName}") { backStackEntry ->
                    val itemName = backStackEntry.arguments?.getString("itemName")
                    val selectedItem = mockItens.first { it.name == itemName }
                    DetailsScreen(selectedItem)
                }
            }
        }
    }
}
