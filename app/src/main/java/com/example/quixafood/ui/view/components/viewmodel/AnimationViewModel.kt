package com.example.quixafood.ui.theme.viewmodel

import AnimationPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnimationViewModel(val animationPreferences: AnimationPreferences) : ViewModel() {

    // Flow para armazenar o estado do modo de animação
    private val _isAnimationMode = MutableStateFlow(false)
    val isAnimationMode: StateFlow<Boolean> = _isAnimationMode

    init {
        // Recupera o estado do modo de animação a partir do DataStore
        viewModelScope.launch {
            animationPreferences.AnimationModeFlow.collect { isAnimationEnabled ->
                _isAnimationMode.value = isAnimationEnabled  // Atualiza o estado do modo de animação
            }
        }
    }

    fun saveAnimationMode(isEnabled: Boolean) {
        viewModelScope.launch {
            animationPreferences.saveAnimationMode(isEnabled)
        }
    }

    fun resetPreferences() {
        viewModelScope.launch {
            animationPreferences.resetPreferences()
        }
    }

}
