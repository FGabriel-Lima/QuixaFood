package com.example.quixafood.ui.view.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.quixafood.model.itemmodel.mockItens

// Função para salvar a preferência do tema nas SharedPreferences
fun saveThemePreference(context: Context, isDarkMode: Boolean) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("isDarkMode", isDarkMode)
    editor.apply()
}
// Função para recuperar a preferência do tema das SharedPreferences
fun getThemePreference(context: Context): Boolean {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isDarkMode", false) // Padrão é false (modo claro)
}

private fun changeNotification(context: Context) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        !checkNotifications(context)
    ) {
        requestNotificationPermission(context)
        Toast.makeText(context, "Permissão necessária para notificações.", Toast.LENGTH_SHORT).show()
        return
    }
    openAppSettings(context)
    Toast.makeText(context, "Revogue a permissão de notificação nas configurações.", Toast.LENGTH_SHORT).show()
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun requestNotificationPermission(context: Context) {
    if (context is Activity) {
        ActivityCompat.requestPermissions(
            context,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            1001
        )
    }
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    if (context is Activity) {
        context.startActivity(intent)
    }
}

private fun checkNotifications(context: Context): Boolean {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
        != PackageManager.PERMISSION_GRANTED) {
        return false
    }
    return true
}

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6)
)

val BlueCalmColorScheme = lightColorScheme(
    primary = Color(0xFF2196F3),
    secondary = Color(0xFF80CBC4),
    tertiary = Color(0xFF81D4FA)
)

val GreenFreshColorScheme = lightColorScheme(
    primary = Color(0xFF4CAF50),
    secondary = Color(0xFF388E3C),
    tertiary = Color(0xFF81C784)
)

val WarmSunsetColorScheme = lightColorScheme(
    primary = Color(0xFFFB8C00),
    secondary = Color(0xFFEF5350),
    tertiary = Color(0xFFFBC02D)
)

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    onThemeToggle: () -> Unit,
    onNotificationsToggle: () -> Unit, // A função que alterna as notificações
) {
    val context = LocalContext.current
    val areNotificationsEnabled = remember { mutableStateOf(checkNotifications(context)) }

    var currentTheme by remember { mutableStateOf(0) } // 0 = Light, 1 = BlueCalm, 2 = GreenFresh, 3 = WarmSunset

    // Aplica o tema de acordo com o valor do estado
    val colorScheme = when (currentTheme) {
        1 -> BlueCalmColorScheme
        2 -> GreenFreshColorScheme
        3 -> WarmSunsetColorScheme
        else -> LightColorScheme
    }

    // Recupera o valor do tema a partir do SharedPreferences
    val isDarkMode = remember { mutableStateOf(getThemePreference(context)) }

    val limparFavoritos = {
        mockItens.forEach { item ->
            if (item.isFavorite.value) {
                item.isFavorite.value = false
            }
        }
        // Exibe mensagem de sucesso
        Toast.makeText(context, "Favoritos Limpos!", Toast.LENGTH_SHORT).show()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red)
    ) {
        Text(
            text = "Tela de Configurações",
            modifier = Modifier
                .padding(16.dp), // Padding interno apenas para o texto
            fontSize = 20.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(70.dp))

        // Modo Escuro
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Modo Escuro",
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isDarkMode.value,
                onCheckedChange = {
                    isDarkMode.value = it // Atualiza o estado do modo escuro

                    onThemeToggle()// Chama a função para alternar o tema
                    saveThemePreference(context, it)// Salva a preferência nas SharedPreferences

                }
            )
        }

        // Notificações
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Notificações",
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = areNotificationsEnabled.value,
                onCheckedChange = {
                    areNotificationsEnabled.value = it
                    changeNotification(context)
                }
            )
        }

        // Animações
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Animações",
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = false, // ou true, dependendo de como você quer que o Switch inicie
                onCheckedChange = { /* Não faz nada aqui */ }
            )



        }

        // Botão Limpar Favoritos
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { limparFavoritos() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE605D))// para deixar esbranquiçado

        ) {
            Text("Limpar Favoritos")
        }

        // Botão Redefinir Preferências
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (isDarkMode.value) {
                    // Redefine o estado do modo escuro para "false" (modo claro)
                    isDarkMode.value = false
                    onThemeToggle() // Chama a função para alternar o tema
                    saveThemePreference(context, false) // Salva a alteração nas SharedPreferences
                }

                if (areNotificationsEnabled.value) {
                    areNotificationsEnabled.value = false // Resetando notificações para ativadas
                    onNotificationsToggle() // Chama a função para alternar as notificações
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE605D))//para deixar esbranquiçado
            ,modifier = Modifier
                .fillMaxWidth()

        ) {
            Text("Redefinir Preferências")
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Tema Atual: ${when (currentTheme) {
                1 -> "Blue Calm"
                2 -> "Green Fresh"
                3 -> "Warm Sunset"
                else -> "Claro"
            }}")

            // Botões para alternar entre os temas
            Button(onClick = { currentTheme = (currentTheme + 1) % 4 }) {
                Text("Trocar Tema")
            }
        }
    }
}
