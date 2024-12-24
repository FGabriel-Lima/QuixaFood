package com.example.quixafood.ui.screens

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
import com.example.quixafood.models.mockItens
import com.example.quixafood.ui.components.ItemCardView
import com.example.quixafood.ui.components.TopAppBarWithMenu




//        if (favoritePlanets.isEmpty()) {
//            // Exibe o texto padrão quando não há favoritos
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding),
//                contentAlignment = Alignment.Center
//            ) {Text(
//                text = "Você ainda não adicionou favoritos.",
//                style = MaterialTheme.typography.titleMedium,
//                textAlign = TextAlign.Center,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//            }
//        } else {
            // Exibe a lista de favoritos
//            LazyColumn(
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .padding(horizontal = 8.dp)
//            ) {
//                items(favoritePlanets) { planet ->
//                    PlanetListItem(
//                        planet = planet,
//                        onPlanetSelected = { onPlanetSelected(it) },
//                        onFavoriteToggle = { onFavoriteToggle(it) }
//                    )
//                }
//            }
//        }
//    }
//}
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