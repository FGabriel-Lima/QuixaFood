package com.example.quixafood.ui.view.screens

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quixafood.R
import com.example.quixafood.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    context: Context
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Obtendo o cliente de autenticação do Google
    val googleSignInClient = remember { authViewModel.getGoogleSignInClient(context) }

    // Lançador para abrir a intent do Google Sign-In
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                authViewModel.loginWithGoogle(idToken) { success ->
                    if (success) {
                        navController.navigate("home")
                    } else {
                        errorMessage = "Erro ao autenticar com o Google"
                    }
                }
            }
        } catch (e: ApiException) {
            errorMessage = "Erro ao fazer login: ${e.localizedMessage}"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Quixafood Logo",

            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Login", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    authViewModel.login(email, password) { success ->
                        if (!success) {
                            errorMessage = "Erro ao fazer login"
                        } else {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = "Entrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Entrar com Google")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Esqueceu a senha?",
            modifier = Modifier.clickable { navController.navigate("resetPassword") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Não tem uma conta? Cadastre-se",
            modifier = Modifier.clickable { navController.navigate("register") }
        )
    }
}