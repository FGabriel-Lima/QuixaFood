package com.example.quixafood.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.quixafood.R
import kotlin.math.round

@ExperimentalMaterial3Api
@Composable
fun TopAppBarWithMenu(
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onHelpClick: () -> Unit,
    onLogoutClick: (Context) -> Unit,
    onSettingsClick: () -> Unit // Adicionando o parâmetro onSettingsClick
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(45.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = "QuixaFood",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Tela Inicial") },
                    onClick = {
                        expanded = false
                        onHomeClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Favoritos") },
                    onClick = {
                        expanded = false
                        onFavoritesClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Ajuda") },
                    onClick = {
                        expanded = false
                        onHelpClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Configurações") },
                    onClick = {
                        expanded = false
                        onSettingsClick()  // Chama a navegação para configurações
                    }
                )
                DropdownMenuItem(
                    text = { Text("Sair") },
                    onClick = {
                        expanded = false
                        onLogoutClick(context)
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
