package com.example.quixafood.ui.view.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomBarItem("home", Icons.Default.Home, "Tela Inicial"),
        BottomBarItem("favorites", Icons.Default.Favorite, "Favoritos"),
        BottomBarItem("search", Icons.Default.Search, "Buscar"),
    )

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavigationBar(
        containerColor = Color.Red
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Controla o comportamento de navegação
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon, // Ícone da tela
                        contentDescription = screen.label, // Descrição do ícone
                        tint = if (currentDestination?.route == screen.route) Color.Yellow else Color.White // Ícone fica amarelo se selecionado
                    )
                },
                label = {
                    Text(
                        text = screen.label,
                        color = if (currentDestination?.route == screen.route) Color.Yellow else Color.White // Texto fica amarelo se o item estiver selecionado
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Yellow, // Ícone selecionado fica amarelo
                    unselectedIconColor = Color.White, // Ícone não selecionado fica branco
                    selectedTextColor = Color.Yellow, // Texto selecionado fica amarelo
                    unselectedTextColor = Color.White, // Texto não selecionado fica branco
                    indicatorColor = Color.Black // Cor da bolinha de fundo ao redor do ícone (quando selecionado)
                )
            )
        }
    }
}

data class BottomBarItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)
