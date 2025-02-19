package com.example.quixafood.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quixafood.data.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    var loginResult: ((Boolean) -> Unit) ?= null
    var registerResult: ((Boolean) -> Unit) ?= null


    fun register(
        email: String,
        password: String,
        name: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val success = repository.registeruser(email, password, name)
            onResult(success)
        }
    }

    fun login(
        email: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val success = repository.loginUser(email, password)
            onResult(success)
        }
    }

    fun resetPassword(
        email: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val success = repository.resetPassword(email)
            onResult(success)
        }

    }

    fun getUserName(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val name = repository.getUserName()
            onResult(name)
        }
    }

    fun logout() {
        repository.logout()
    }

    fun isuserlogged(): Boolean {
        return repository.isuserlogged()
    }

}