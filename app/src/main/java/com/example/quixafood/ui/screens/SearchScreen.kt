package com.example.quixafood.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quixafood.R

data class Item(val name: String, val imageRes: Int)

val itemList = listOf(
    Item("Pizza", R.drawable.pizza),
    Item("Hambúrguer", R.drawable.hamburguer),
    Item("Sushi", R.drawable.sushi),
    Item("Salada", R.drawable.salada),
    Item("Frango", R.drawable.frango),
    Item("Coca-Cola", R.drawable.cocacola)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = itemList.filter { it.name.contains(searchQuery, ignoreCase = true) }

    Column(modifier = Modifier.padding(16.dp)) {

        // Campo de busca
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar", fontWeight = FontWeight.Bold) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Ícone de busca"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpar busca")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.Gray
            )
        )

        // Exibe mensagem quando não encontrar nenhum item
        if (filteredItems.isEmpty()) {
            Text(
                "Nenhum item encontrado.",
                modifier = Modifier.padding(top = 8.dp),
                color = Color.Gray
            )
        } else {
            // Lista de alimentos filtrados
            LazyColumn {
                items(filteredItems) { item ->
                    // Card com imagem e nome do item
                    SearchItemCard(item = item)
                }
            }
        }
    }
}

@Composable
fun SearchItemCard(item: Item) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagem do item
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp)
            )

            // Nome do item
            Column {
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        // Exibe o Toast aqui
                        Toast.makeText(context, "${item.name} selecionado", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.padding(top = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text(
                        text = "Adicionar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
