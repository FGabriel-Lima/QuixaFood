package com.example.quixafood.model.navigation

import AnimationPreferences
import ThemePreferences
import com.example.quixafood.ui.theme.viewmodel.ThemeViewModel
import com.example.quixafood.ui.theme.viewmodel.ThemeViewModelFactory
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quixafood.data.AuthRepository
import com.example.quixafood.model.itemmodel.mockItens
import com.example.quixafood.ui.view.components.BottomNavigationBar
import com.example.quixafood.ui.view.screens.DetailsScreen
import com.example.quixafood.ui.view.screens.FavoritesScreen
import com.example.quixafood.ui.view.screens.HelpScreen
import com.example.quixafood.ui.view.screens.HomeScreen
import com.example.quixafood.ui.view.screens.LoginScreen
import com.example.quixafood.ui.view.screens.RegisterScreen
import com.example.quixafood.ui.view.screens.ResetPasswordScreen
import com.example.quixafood.ui.view.screens.SearchScreen
import com.example.quixafood.ui.view.screens.SettingsScreen
import com.example.quixafood.ui.theme.QuixaFoodTheme
import com.example.quixafood.ui.theme.viewmodel.AnimationViewModel
import com.example.quixafood.ui.theme.viewmodel.AnimationViewModelFactory
import com.example.quixafood.viewmodel.AuthViewModel
import java.time.LocalTime

sealed class BottomBarScreen(val route: String, val icon: @Composable () -> Unit, val label: String) {
    object Home : BottomBarScreen(
        route = "home",
        icon = { Icon(Icons.Default.Home, contentDescription = "Tela Inicial") },
        label = "Tela Inicial"
    )
    object Favorites : BottomBarScreen(
        route = "favorites",
        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
        label = "Favoritos"
    )
    object Help : BottomBarScreen(
        route = "help",
        icon = { Icon(Icons.Default.Info, contentDescription = "Ajuda") },
        label = "Ajuda"
    )
    object Search : BottomBarScreen(
        route = "search",
        icon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        label = "Buscar"
    )
    object Settings : BottomBarScreen( // Tela de Configurações
        route = "settings",
        icon = { Icon(Icons.Default.Settings, contentDescription = "Configurações") },
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
    val themePreferences = remember { ThemePreferences(context) }
    val themeViewModel: ThemeViewModel = viewModel(factory = ThemeViewModelFactory(themePreferences))

    val isDarkTheme by themeViewModel.isDarkMode.collectAsState()
    val isAutoDarkMode by themeViewModel.isAutoDarkMode.collectAsState()
    val isNotificationsEnabled = remember { mutableStateOf(false) }
    val animationPreferences = remember { AnimationPreferences(context) }
    val animationViewModel: AnimationViewModel = viewModel(factory = AnimationViewModelFactory(animationPreferences))

    // 🔹 Atualiza o tema automaticamente se o modo automático estiver ativado
    LaunchedEffect(isAutoDarkMode) {
        Log.d("NavGraph", "Auto Dark Mode is ${isAutoDarkMode}")  // Log para verificar o valor do modo automático
        themeViewModel.checkAndUpdateDarkMode()
    }

    val authViewModel = AuthViewModel(AuthRepository())

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    val noBottomBarScreens = listOf("login", "register", "resetPassword")

    QuixaFoodTheme( themeViewModel = themeViewModel) {
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

                    SearchScreen(
                        navController = navController,
                        animationViewModel = animationViewModel,
                    )
                }

                // Tela de Configurações
                composable(BottomBarScreen.Settings.route) {
                    SettingsScreen(
                        themeViewModel,
                        animationViewModel,
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
