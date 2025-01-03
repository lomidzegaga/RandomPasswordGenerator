package com.example.passwordgenerator

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GeneratePasswordVM : ViewModel() {

    private val _generatedPassword = MutableStateFlow("")
    val generatedPassword = _generatedPassword.asStateFlow()

    var upperCase = mutableStateOf(false)
    var lowerCase = mutableStateOf(false)
    var numbers = mutableStateOf(false)
    var symbols = mutableStateOf(false)
    var passwordSize = mutableIntStateOf(2)

    fun generatePassword() {
        val generatePassword = StringBuilder()
        repeat(passwordSize.intValue) {
            generatePassword.append("A-Z".random())
        }
        _generatedPassword.value = generatePassword.toString()
    }
}