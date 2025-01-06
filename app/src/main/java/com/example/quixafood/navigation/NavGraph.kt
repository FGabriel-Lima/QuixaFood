package com.example.quixafood.navigation

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quixafood.ui.components.BottomNavigationBar
import com.example.quixafood.ui.screens.FavoritesScreen
import com.example.quixafood.ui.screens.HelpScreen
import com.example.quixafood.ui.screens.HomeScreen
import com.example.quixafood.ui.screens.SearchScreen
import com.example.quixafood.ui.screens.SettingsScreen

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

@ExperimentalMaterial3Api
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Tela Home
            composable(BottomBarScreen.Home.route) {
                HomeScreen(
                    onHomeClick = {
                        navController.navigate(BottomBarScreen.Home.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onFavoritesClick = {
                        navController.navigate(BottomBarScreen.Favorites.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onHelpClick = {
                        navController.navigate(BottomBarScreen.Help.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onLogoutClick = { context: Context ->
                        Toast.makeText(context, "Logout realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    },
                    onSettingsClick = {
                        navController.navigate(BottomBarScreen.Settings.route)
                    }
                )
            }

            // Tela de Favoritos
            composable(BottomBarScreen.Favorites.route) {
                FavoritesScreen()
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
                SettingsScreen()
            }

        }
    }
}

