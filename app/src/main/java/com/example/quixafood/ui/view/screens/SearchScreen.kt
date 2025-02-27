package com.example.quixafood.ui.view.screens

import android.annotation.SuppressLint
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
import com.example.quixafood.model.itemmodel.Itens
import com.example.quixafood.model.itemmodel.mockItens
import com.example.quixafood.ui.view.components.PacManProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // Controla o estado de carregamento
    var progress by remember { mutableStateOf(0f) } // Progresso da barra
    val filteredItems by derivedStateOf {
        mockItens.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }
    val coroutineScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) } // Gerencia a coroutine ativa

    Column(modifier = Modifier.padding(16.dp)) {

        // Campo de busca
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                isLoading = true
                progress = 0f

                // Cancela qualquer busca em andamento
                searchJob?.cancel()
                searchJob = coroutineScope.launch {
                    val totalDuration = 4000L // Duração de 4 segundos
                    val stepDuration = 50L // Atualização a cada 50ms
                    val steps = totalDuration / stepDuration

                    repeat(steps.toInt()) {
                        delay(stepDuration)
                        progress = (progress + 1f / steps).coerceIn(0f, 1f) // Limita entre 0 e 1
                    }
                    isLoading = false // Finaliza o carregamento
                }
            },
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

        // Barra de carregamento
        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            PacManProgressBar(progress = progress)
        }

        // Exibe mensagem quando não encontrar nenhum item
        if (!isLoading && filteredItems.isEmpty()) {
            Text(
                "Nenhum item encontrado.",
                modifier = Modifier.padding(top = 8.dp),
                color = Color.Gray
            )
        }

        // Lista de itens filtrados
        if (!isLoading) {
            LazyColumn {
                items(filteredItems) { item ->
                    SearchItemCard(item = item)
                }
            }
        }
    }
}

@Composable
fun SearchItemCard(item: Itens) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
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

            // Nome, descrição e preço do item
            Column {
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "R$ ${item.price}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
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
