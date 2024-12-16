package com.example.quixafood.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.quixafood.ui.components.TopAppBarWithMenu

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onLogoutClick: (Context) -> Unit
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
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = "Tela de Home")
        }
    }
}