package com.example.quixafood.ui.view.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(navController: NavHostController) {
    var message by remember { mutableStateOf(TextFieldValue("")) }
    var showMessageConfirmation by remember { mutableStateOf(false) }

    // Lista de perguntas frequentes
    val faqs = listOf(
        "Como faço um pedido no QuixaFood?",
        "Posso cancelar um pedido em andamento?",
        "Quais métodos de pagamento são aceitos?",
        "Como entro em contato com o restaurante?",
        "Posso agendar um pedido para outra hora?"
    )

    // Paleta de cores personalizada
    val primaryColor = Color(0xFFD32F2F) // Vermelho
    val secondaryColor = Color(0xFFFFC107) // Amarelo
    val tertiaryColor = Color(0xFF000000) // Preto

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajuda e Suporte", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = primaryColor)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Lista de perguntas frequentes
            Text(
                text = "Perguntas Frequentes:",
                style = MaterialTheme.typography.titleMedium,
                color = tertiaryColor, // Preto
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(faqs.size) { index ->
                    Text(
                        text = "• ${faqs[index]}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = tertiaryColor, // Preto
                        modifier = Modifier.padding(vertical = 10.dp) // 4 dp
                    )
                }
            }

            // Campo para envio de mensagens
            Text(
                text = "Envie sua mensagem ao suporte:",
                style = MaterialTheme.typography.titleMedium,
                color = tertiaryColor, // Preto
                modifier = Modifier.padding(vertical = 8.dp)
            )
            BasicTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(120.dp)
                    .border(1.dp, secondaryColor) // Amarelo
                    .padding(8.dp)
            )

            // Botão para enviar mensagem
            Button(
                onClick = {
                    showMessageConfirmation = true
                    message = TextFieldValue("") // Limpar campo após envio
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor, // Fundo vermelho
                    contentColor = Color.White // Texto branco
                )
            ) {
                Text(text = "Enviar Mensagem")
            }

            // Exibição de mensagem de confirmação
            if (showMessageConfirmation) {
                Text(
                    text = "Mensagem enviada com sucesso!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Green, // Verde para confirmar envio
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
