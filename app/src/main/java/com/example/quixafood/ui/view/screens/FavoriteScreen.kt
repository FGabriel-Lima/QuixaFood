package com.example.quixafood.ui.view.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.quixafood.model.itemmodel.mockItens
import com.example.quixafood.ui.view.components.ItemCardView
import com.example.quixafood.ui.view.components.TopAppBarWithMenu

@ExperimentalMaterial3Api
@Composable
fun FavoritesScreen(
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onLogoutClick: (Context) -> Unit,
    navController: NavController,
    navigateTo: (NavController, String, Boolean, Boolean, Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarWithMenu(
                onHomeClick = onHomeClick,
                onFavoritesClick = onFavoritesClick,
                onSettingsClick = onSettingsClick,
                onHelpClick = onHelpClick,
                onLogoutClick = onLogoutClick
            )
        }
    ) { innerPadding ->
        val favoriteItems = mockItens.filter { it.isFavorite.value }
        if (favoriteItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Você ainda não adicionou favoritos."
                )
            }
        } else {
            Column(modifier = Modifier.padding(innerPadding)) {
                LazyColumn {
                    items(favoriteItems) { item ->
                        ItemCardView(
                            itens = item,
                            onFavoriteClick = {
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
}