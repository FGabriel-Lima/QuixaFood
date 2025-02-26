package com.example.quixafood.model.itemmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.quixafood.R

data class Itens(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageRes: Int,
    val isFavorite: MutableState<Boolean> = mutableStateOf(false)
);

val mockItens = listOf(
    Itens(
        id = "1",
        name = "Hambúrguer",
        description = "Hamburguer de carne com queijo, alface e tomate",
        price = 15.0,
        imageRes = R.drawable.hamburguer,
    ),
    Itens(
        id = "2",
        name = "Pizza",
        description = "Pizza de calabresa com queijo e molho de tomate",
        price = 25.0,
        imageRes = R.drawable.pizza,
    ),
    Itens(
        id = "3",
        name = "Coca-cola",
        description = "Refrigerante de cola",
        price = 5.0,
        imageRes = R.drawable.cocacola,
    ),
    Itens(
        id = "4",
        name = "Cerveja",
        description = "Cerveja pilsen",
        price = 7.0,
        imageRes = R.drawable.cerveja,
    ),
    Itens(
        id = "5",
        name = "Sorvete",
        description = "Sorvete de creme",
        price = 10.0,
        imageRes = R.drawable.sorvete,
    )
)