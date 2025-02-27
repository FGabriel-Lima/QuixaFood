package com.example.quixafood.ui.theme.viewmodel

import ThemePreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ColorScheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import com.example.quixafood.ui.theme.*

class ThemeViewModel(val themePreferences: ThemePreferences) : ViewModel() {

    // Flow para armazenar o estado do tema
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    private val _isAutoDarkMode = MutableStateFlow(false) // Novo estado para modo automático
    val isAutoDarkMode: StateFlow<Boolean> = _isAutoDarkMode
    private var isManualDarkModeSet = false  // Flag para verificar se o modo escuro manual foi alterado

    private val _colorScheme = MutableStateFlow<ColorScheme>(LightColorScheme)
    val colorScheme: StateFlow<ColorScheme> = _colorScheme

    // Função atualizada para alterar o esquema de cores
    fun setColorScheme(scheme: ColorScheme) {
        _colorScheme.value = scheme
        // Atualiza o estado do modo escuro se o esquema de cores for o modo escuro
        _isDarkMode.value = scheme == DarkColorScheme

        // Salva o esquema de cores no DataStore
        viewModelScope.launch {
            val schemeName = when (scheme) {
                DarkColorScheme -> "dark"
                BlueCalmColorScheme -> "blue"
                GreenFreshColorScheme -> "green"
                WarmSunsetColorScheme -> "warm"
                else -> "light"
            }
            themePreferences.saveColorScheme(schemeName)
        }
    }

    init {
        // Recupera o estado do modo escuro e modo automático a partir do DataStore
        viewModelScope.launch {
            themePreferences.autoDarkModeFlow.collect { isAutoDark ->
                if (isAutoDark && !isManualDarkModeSet) {  // Se o modo automático estiver ativado, e o manual não foi alterado, aplica automaticamente
                    _isDarkMode.value = isNightMode()  // Altere o valor de isDarkMode com base na hora
                }
                _isAutoDarkMode.value = isAutoDark  // Atualiza o estado do modo automático

                // Adicionando a coleta do estado do modo escuro
                themePreferences.darkModeFlow.collect { isDark ->
                    if (!isManualDarkModeSet) {  // Se o modo manual não foi alterado, mantém o valor
                        _isDarkMode.value = isDark
                    }
                }

                // Adicionando a coleta do esquema de cores
                themePreferences.colorSchemeFlow.collect { scheme ->
                    _colorScheme.value = when (scheme) {
                        "dark" -> DarkColorScheme
                        "blue" -> BlueCalmColorScheme
                        "green" -> GreenFreshColorScheme
                        "warm" -> WarmSunsetColorScheme
                        else -> LightColorScheme
                    }
                }
            }
        }
    }

    // Alterna o tema e salva no DataStore
    fun toggleDarkMode() {
        viewModelScope.launch {
            isManualDarkModeSet = true  // Indica que o usuário alterou o modo manualmente
            val newMode = !_isDarkMode.value
            themePreferences.saveDarkMode(newMode)  // Salva no DataStore
            _isDarkMode.value = newMode
            // Atualiza o esquema de cores de acordo com o novo estado
            setColorScheme(if (newMode) DarkColorScheme else LightColorScheme)
        }
    }

    // Ativa ou desativa o modo automático e salva no DataStore
    fun setAutoDarkMode(enabled: Boolean) {
        isManualDarkModeSet = false  // Indica que o modo manual não foi alterado, então o automático pode ser usado
        _isAutoDarkMode.value = enabled
        viewModelScope.launch {
            themePreferences.saveAutoDarkMode(enabled)  // Salva no DataStore
        }
        if (enabled) {
            _isDarkMode.value = isNightMode()  // Atualiza o tema automaticamente com base na hora
            setColorScheme(if (_isDarkMode.value) DarkColorScheme else LightColorScheme)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isNightMode(): Boolean {
        val now = LocalTime.now()
        return now.hour in 19..23 || now.hour in 0..5  // Definição para o modo noturno
    }

    // Verifica e atualiza o modo escuro de acordo com o modo automático
    fun checkAndUpdateDarkMode() {
        if (_isAutoDarkMode.value && !isManualDarkModeSet) {
            _isDarkMode.value = isNightMode()  // Atualiza o tema automaticamente
            setColorScheme(if (_isDarkMode.value) DarkColorScheme else LightColorScheme)
        }
    }

    // Função para redefinir as preferências
    fun resetPreferences() {
        viewModelScope.launch {
            themePreferences.resetPreferences()
            _isDarkMode.value = false // Redefine o modo escuro para false
            _isAutoDarkMode.value = false // Redefine o modo automático para false
            setColorScheme(LightColorScheme) // Define o esquema de cores padrão
        }
    }

    // Função para alterar entre esquemas de cores personalizados
    fun changeCustomColorScheme(scheme: ColorScheme) {
        setColorScheme(scheme)
    }
}
