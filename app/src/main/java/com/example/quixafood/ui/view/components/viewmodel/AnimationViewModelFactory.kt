package com.example.quixafood.ui.theme.viewmodel

import AnimationPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AnimationViewModelFactory(private val animationPreferences: AnimationPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AnimationViewModel(animationPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
