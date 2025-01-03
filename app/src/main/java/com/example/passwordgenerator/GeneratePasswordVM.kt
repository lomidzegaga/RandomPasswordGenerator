package com.example.passwordgenerator

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GeneratePasswordVM @Inject constructor() : ViewModel() {

    var password = mutableStateOf("").value
        private set

    var passwordLength = mutableFloatStateOf(12f).floatValue
        private set

    val characters = mutableStateListOf<Characters>(
        Characters.UpperCase(), Characters.LowerCase(), Characters.Numbers(), Characters.Symbols()
    )

    fun generatePassword() {
        password = generatePassword(characters, passwordLength.toInt())
    }

    fun updatePasswordLength(length: Float) {
        passwordLength = length
        generatePassword()
    }

    fun toggleCharacter(char: Characters, isChecked: Boolean) {
        if (isChecked) characters.add(char) else characters.remove(char)
        generatePassword()
    }

    fun getPasswordStrength(): PasswordStrength = when (password.length) {
        in 1 until 5 -> PasswordStrength.VERY_WEAK
        in 5 until 8 -> PasswordStrength.WEAK
        8, 9 -> PasswordStrength.GOOD
        10, 11 -> PasswordStrength.STRONG
        else -> PasswordStrength.VERY_STRONG
    }
}