package com.example.quixafood.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.quixafood.ui.theme.viewmodel.ThemeViewModel
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

// Esquema de cores escuras
val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Color.White, // Texto em cima do fundo primário
    secondary = PurpleGrey80,
    onSecondary = Color.Black,
    tertiary = Pink80,
    onTertiary = Color.Black,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color(0xFF121212), // Cor de fundo para superfícies
    onSurface = Color.White,
    error = Color.Red,
    onError = Color.White
)

// Esquema de cores claras
val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    secondary = PurpleGrey40,
    onSecondary = Color.Black,
    tertiary = Pink40,
    onTertiary = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color(0xFFFFFFFF),
    onSurface = Color.Black,
    error = Color.Red,
    onError = Color.White
)

// Esquema de cores azuis
val BlueCalmColorScheme = lightColorScheme(
    primary = Blue500,
    onPrimary = Color.White,
    secondary = Teal200,
    onSecondary = Color.Black,
    tertiary = LightBlue200,
    onTertiary = Color.Black,
    background = Color(0xFFE3F2FD), // Azul claro para fundo
    onBackground = Color.Black,
    surface = Color(0xFFBBDEFB),
    onSurface = Color.Black,
    error = Color.Red,
    onError = Color.White
)

// Esquema de cores verdes
val GreenFreshColorScheme = lightColorScheme(
    primary = Green500,
    onPrimary = Color.White,
    secondary = Lime700,
    onSecondary = Color.Black,
    tertiary = LightGreen300,
    onTertiary = Color.Black,
    background = Color(0xFFE8F5E9), // Verde claro para fundo
    onBackground = Color.Black,
    surface = Color(0xFFC8E6C9),
    onSurface = Color.Black,
    error = Color.Red,
    onError = Color.White
)

// Esquema de cores quentes
val WarmSunsetColorScheme = lightColorScheme(
    primary = Orange400,
    onPrimary = Color.White,
    secondary = Red200,
    onSecondary = Color.White,
    tertiary = Yellow700,
    onTertiary = Color.Black,
    background = Color(0xFFFFF3E0), // Laranja claro para fundo
    onBackground = Color.Black,
    surface = Color(0xFFFFE0B2),
    onSurface = Color.Black,
    error = Color.Red,
    onError = Color.White
)

@Composable
fun QuixaFoodTheme(
    themeViewModel: ThemeViewModel,
    content: @Composable () -> Unit
) {
    val colorScheme by themeViewModel.colorScheme.collectAsState()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

//@Composable
//fun QuixaFoodTheme(
//    themeViewModel: ThemeViewModel,
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    dynamicColor: Boolean = true,
//    content: @Composable () -> Unit
//) {
//    val colorScheme by themeViewModel.colorScheme.collectAsState()
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}