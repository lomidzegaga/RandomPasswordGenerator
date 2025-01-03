package com.example.passwordgenerator

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

val veryWeakPasswordColor = Color(0xFFFD7700)
val weakPasswordColor = Color(0xFFFDB26F)
val goodPasswordColor = Color(0xFFFDDBBE)
val strongPasswordColor = Color(0xFFD3F0A4)
val veryStrongPasswordColor = Color(0xFF99E237)

val buttonBlueColor = Color(0xFF006FF4)
val lightBlue = Color(0xFF3C91F6)

val black = Color(0xFF000000)
val white = Color(0xFFFFFFFF)
val green = Color(0xFF00FF00)
val gray = Color(0xFF888888)


sealed interface Characters {
    data class UpperCase(val value: CharRange = ('A'..'Z')) : Characters
    data class LowerCase(val value: CharRange = ('a'..'z')) : Characters
    data class Numbers(val value: CharRange = ('0'..'9')) : Characters
    data class Symbols(val value: String = "!@#$%^&*()_-+") : Characters
}

enum class PasswordStrength(
    val color: Color,
    val text: String,
    @DrawableRes val image: Int
) {
    VERY_WEAK(veryWeakPasswordColor, "Very Weak", R.drawable.very_week),
    WEAK(weakPasswordColor, "Weak", R.drawable.week),
    GOOD(goodPasswordColor, "Good", R.drawable.good),
    STRONG(strongPasswordColor, "Strong", R.drawable.strong),
    VERY_STRONG(veryStrongPasswordColor, "Very Strong", R.drawable.very_strong)
}

fun generatePassword(
    chars: List<Characters>, length: Int
): String = (1..length).map {
    val randomCharSet = chars.random()
    generatePassword(randomCharSet)
}.joinToString("")


fun generatePassword(char: Characters): Char = when (char) {
    is Characters.UpperCase -> char.value.random()
    is Characters.LowerCase -> char.value.random()
    is Characters.Numbers -> char.value.random()
    is Characters.Symbols -> char.value.random()
}

class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = MutableSharedFlow()

    override suspend fun emit(interaction: Interaction) = Unit

    override fun tryEmit(interaction: Interaction): Boolean = false
}