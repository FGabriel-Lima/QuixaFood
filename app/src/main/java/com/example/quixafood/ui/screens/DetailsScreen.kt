package com.example.quixafood.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.quixafood.models.Itens

@ExperimentalMaterial3Api
@Composable
fun DetailsScreen(item: Itens) {
    Text(text = "Tela de Detalhes do Item: "+item.name)
}