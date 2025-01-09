package com.example.quixafood.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.quixafood.models.mockItens
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavController
import com.example.quixafood.ui.components.ItemCardView
import com.example.quixafood.ui.components.TopAppBarWithMenu

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onHelpClick: () -> Unit,
    onLogoutClick: (Context) -> Unit,
    onSettingsClick: () -> Unit,
    navController: NavController,
    navigateTo: (NavController, String, Boolean, Boolean, Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarWithMenu(
                onHomeClick = onHomeClick,
                onFavoritesClick = onFavoritesClick,
                onHelpClick = onHelpClick,
                onLogoutClick = onLogoutClick,
                onSettingsClick = onSettingsClick  // Passando para o TopAppBar
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                items(mockItens) { item ->
                    ItemCardView(itens = item, onFavoriteClick = {
                        item.isFavorite.value = !item.isFavorite.value
                    },
                    onDetailClick = {
                        navigateTo(navController, "details/${item.name}", true, true, false)
                    })
                }
            }
        }
    }
}
