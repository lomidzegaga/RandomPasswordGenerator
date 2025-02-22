package com.example.passwordgenerator

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.passwordgenerator.composables.Characters
import com.example.passwordgenerator.composables.CopyButton
import com.example.passwordgenerator.composables.PasswordLengthSlider
import com.example.passwordgenerator.composables.PasswordTextField

private const val passwordStrengthImage = "password Strength image"
private const val toastText = "copied"
private const val passwordLength = "Password length:"
private const val charactersUsed = "Characters used:"

@Composable
fun RandomPasswordGenerator(
    modifier: Modifier = Modifier,
    viewModel: GeneratePasswordVM = hiltViewModel()
) {
    val clipBoardManager = LocalClipboardManager.current
    val context = LocalContext.current

    viewModel.apply {
        RandomPasswordGeneratorComposable(
            password = password.value,
            sliderValue = passwordLength.floatValue,
            onCopyButtonClick = {
                clipBoardManager.setText(AnnotatedString(password.value))
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            },
            passwordStrength = ::getPasswordStrength,
            resetPassword = ::generatePassword,
            onSliderValueChange = ::updatePasswordLength,
            onCheckboxClick = ::toggleCharacter,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RandomPasswordGeneratorComposable(
    password: String,
    sliderValue: Float,
    passwordStrength: () -> PasswordStrength,
    onCopyButtonClick: () -> Unit,
    resetPassword: () -> Unit,
    onSliderValueChange: (Float) -> Unit,
    onCheckboxClick: (Characters, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Image(
            painter = painterResource(id = passwordStrength().image),
            contentDescription = passwordStrengthImage
        )

        PasswordTextField(
            password = password,
            passwordStrength = passwordStrength(),
            resetPassword = resetPassword
        )

        CopyButton(onCopyButtonClick = onCopyButtonClick)

        Text(text = "$passwordLength ${sliderValue.toInt()}")

        PasswordLengthSlider(
            sliderValue = sliderValue,
            onSliderValueChange = onSliderValueChange
        )

        Text(text = charactersUsed)

        Characters(onCheckboxClick = onCheckboxClick)
    }
}

@Composable
fun LineAndCheckmarkAnimation(
    rectSize: Float = 200f
) {
    val lineLength = 100f // Length of the moving line
    val perimeter = 2 * (rectSize + rectSize)

    // State for controlling the animation stages
    val isLineAnimationComplete = remember { mutableStateOf(false) }
    val checkmarkProgress = remember { Animatable(0f) }

    // Animate the line around the rectangle
    val lineAnimationProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        lineAnimationProgress.animateTo(
            targetValue = 1f, animationSpec = tween(durationMillis = 4000, easing = LinearEasing)
        )
        isLineAnimationComplete.value = true

        // Start the checkmark animation after the line finishes
        checkmarkProgress.animateTo(
            targetValue = 1f, animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val rectLeft = center.x - rectSize / 2
        val rectTop = center.y - rectSize / 2
        val rectRight = center.x + rectSize / 2
        val rectBottom = center.y + rectSize / 2

        // Helper function to get position smoothly on the border
        fun getPositionAlongBorder(offset: Float): Offset {
            val adjustedOffset = offset % perimeter
            return when {
                adjustedOffset <= rectSize -> // Top edge
                    Offset(rectRight + adjustedOffset, rectTop)

                adjustedOffset <= rectSize + rectSize -> // Right edge
                    Offset(rectRight, rectTop + (adjustedOffset - rectSize))

                adjustedOffset <= 2 * rectSize + rectSize -> // Bottom edge
                    Offset(rectRight - (adjustedOffset - rectSize - rectSize), rectBottom)

                else -> // Left edge
                    Offset(rectLeft, rectBottom - (adjustedOffset - 2 * rectSize - rectSize))
            }
        }

        // Draw the rectangle
        drawRect(
            color = Color.Gray,
            topLeft = Offset(rectLeft, rectTop),
            size = Size(rectSize, rectSize),
            style = Stroke(width = 4.dp.toPx())
        )

        // Draw the line around the rectangle
        if (!isLineAnimationComplete.value) {
            val progress = lineAnimationProgress.value * perimeter
            val startOffset = progress
            val endOffset = (progress + lineLength)

            val segments = mutableListOf<Offset>()
            var currentOffset = startOffset
            while (currentOffset <= endOffset) {
                segments.add(getPositionAlongBorder(currentOffset))
                currentOffset += 5f // Granularity of the segments
            }
            segments.add(getPositionAlongBorder(endOffset))

            // Draw the moving line
            for (i in 0 until segments.size - 1) {
                drawLine(
                    color = Color.Red, strokeWidth = 8f, start = segments[i], end = segments[i + 1]
                )
            }
        }

        // Draw the checkmark inside the rectangle
        if (isLineAnimationComplete.value) {
            val checkmarkPath = Path().apply {
                val startX = rectLeft + rectSize / 4
                val startY = rectTop + rectSize / 2
                val midX = startX + rectSize / 8
                val midY = startY + rectSize / 4
                val endX = rectLeft + 3 * rectSize / 4
                val endY = rectTop + rectSize / 4

                moveTo(startX, startY)
                lineTo(midX, midY)
                lineTo(endX, endY)
            }

            val pathMeasure = PathMeasure()
            pathMeasure.setPath(checkmarkPath, false)
            val checkmarkPathLength = pathMeasure.length
            val drawPath = Path()

            pathMeasure.getSegment(
                startDistance = 0f,
                stopDistance = checkmarkProgress.value * checkmarkPathLength,
                destination = drawPath
            )

            drawPath(
                path = drawPath, color = Color.Green, style = Stroke(width = 8f)
            )
        }
    }
}

@Preview
@Composable
private fun RandomPasswordGeneratorComposablePrev() {
    RandomPasswordGeneratorComposable(
        password = "",
        passwordStrength = { PasswordStrength.VERY_WEAK },
        modifier = Modifier.background(Color.White),
        sliderValue = 12f,
        onCopyButtonClick = { },
        resetPassword = { },
        onSliderValueChange = { },
        onCheckboxClick = { _, _ -> })
}