package com.example.quixafood.ui.view.screens

import com.example.quixafood.ui.theme.viewmodel.ThemeViewModel
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.quixafood.ui.theme.viewmodel.AnimationViewModel

import com.example.quixafood.ui.theme.DarkColorScheme
import com.example.quixafood.ui.theme.LightColorScheme
import com.example.quixafood.ui.theme.BlueCalmColorScheme
import com.example.quixafood.ui.theme.GreenFreshColorScheme
import com.example.quixafood.ui.theme.WarmSunsetColorScheme
import kotlin.math.log


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

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
    themeViewModel: ThemeViewModel,
    animationViewModel: AnimationViewModel
) {
    val context = LocalContext.current
    val areNotificationsEnabled = remember { mutableStateOf(checkNotifications(context)) }

    val isDarkMode by themeViewModel.isDarkMode.collectAsState()
    val isAutoDarkMode by themeViewModel.isAutoDarkMode.collectAsState()
    val isAnimationMode by animationViewModel.isAnimationMode.collectAsState()

    val limparFavoritos = {
        mockItens.forEach { item ->
            if (item.isFavorite.value) {
                item.isFavorite.value = false
            }
        }
        Toast.makeText(context, "Favoritos Limpos!", Toast.LENGTH_SHORT).show()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red)
    ) {
        Text(
            text = "Tela de Configurações",
            modifier = Modifier.padding(16.dp),
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

        // Modo Escuro Automático
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text(text = "Modo Escuro Automático", modifier = Modifier.weight(1f))
            Switch(
                checked = isAutoDarkMode,
                onCheckedChange = { isEnabled -> themeViewModel.setAutoDarkMode(isEnabled) }
            )
        }

        // Modo Escuro Manual
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text(text = "Modo Escuro Manual", modifier = Modifier.weight(1f))
            Switch(
                checked = isDarkMode,
                enabled = !isAutoDarkMode,
                onCheckedChange = { themeViewModel.toggleDarkMode() }
            )
        }

        // Notificações
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text(text = "Notificações", modifier = Modifier.weight(1f))
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
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text(text = "Animações", modifier = Modifier.weight(1f))
            Switch(
                checked = isAnimationMode,
                onCheckedChange = { isEnabled -> animationViewModel.saveAnimationMode(isEnabled) }

            )
        }

        // Botão Limpar Favoritos
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { limparFavoritos() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE605D))
        ) {
            Text("Limpar Favoritos")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                themeViewModel.resetPreferences()
                animationViewModel.resetPreferences()
                areNotificationsEnabled.value=false //não funciona nem o verificar notificações
                if(!areNotificationsEnabled.value){
                    changeNotification(context)
                    areNotificationsEnabled.value=false
                }
                Toast.makeText(context, "Preferências redefinidas para o padrão.", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE605D)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Redefinir Preferências")
        }

        Spacer(modifier = Modifier.height(16.dp))

        val colorSchemes = listOf(
            LightColorScheme,
            DarkColorScheme,
            BlueCalmColorScheme,
            GreenFreshColorScheme,
            WarmSunsetColorScheme
        )

        var currentThemeIndex by remember { mutableStateOf(0) }



        Button(
            onClick = {
                // Incrementa o índice e redefine se ultrapassar o tamanho da lista
                currentThemeIndex = (currentThemeIndex + 1) % colorSchemes.size
                themeViewModel.setColorScheme(colorSchemes[currentThemeIndex])
                Log.d("ThemeToggleButton", "Tema alterado para: ${colorSchemes[currentThemeIndex]}")
                //Toast.makeText(LocalContext.current, "Tema alterado", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFE605D)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mudar Tema")
        }
    }
}

