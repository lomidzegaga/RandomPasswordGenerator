package com.example.passwordgenerator

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun RandomPasswordGenerator(
    modifier: Modifier = Modifier
) {
    val list = remember { mutableStateListOf<Characters>(Characters.UpperCase()) }
    var password by remember { mutableStateOf("adWddsasddasad") }

    var passwordStrengthColor by remember { mutableStateOf(Color.Green) }
    var passwordStrengthText by remember { mutableStateOf("") }
    var passwordStrengthImage by remember { mutableIntStateOf(R.drawable.strong) }
    var passwordLength by remember { mutableFloatStateOf(12f) }

    LaunchedEffect(passwordLength) {
        when (password.length) {
            in 1 until 5 -> {
                passwordStrengthColor = veryWeakPasswordColor
                passwordStrengthText = "Very Weak"
                passwordStrengthImage = R.drawable.very_week
            }

            in 5 until 8 -> {
                passwordStrengthColor = weakPasswordColor
                passwordStrengthText = "Weak"
                passwordStrengthImage = R.drawable.week
            }

            8, 9 -> {
                passwordStrengthColor = goodPasswordColor
                passwordStrengthText = "Good"
                passwordStrengthImage = R.drawable.good
            }

            10, 11 -> {
                passwordStrengthColor = strongPasswordColor
                passwordStrengthText = "Strong"
                passwordStrengthImage = R.drawable.strong
            }

            else -> {
                passwordStrengthColor = veryStrongPasswordColor
                passwordStrengthText = "Very Strong"
                passwordStrengthImage = R.drawable.very_strong
            }
        }
    }

    LaunchedEffect(passwordLength, list.size) {
        password = generatePassword(list, passwordLength.toInt())
    }

    RandomPasswordGeneratorComposable(
        password = password,
        passwordStrengthText = passwordStrengthText,
        color = passwordStrengthColor,
        image = passwordStrengthImage,
        sliderValue = passwordLength,
        copyButtonClick = {},
        onSliderValueChange = { passwordLength = it },
        onCheckboxClick = { isChecked, char ->
            if (isChecked) list.add(char) else list.remove(char)
        },
        modifier = modifier
    )
}

fun generatePassword(
    chars: List<Characters>,
    length: Int
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

sealed interface Characters {
    data class UpperCase(val value: CharRange = ('A'..'Z')) : Characters
    data class LowerCase(val value: CharRange = ('a'..'z')) : Characters
    data class Numbers(val value: CharRange = ('0'..'9')) : Characters
    data class Symbols(val value: String = "!@#$%^&*()_-+") : Characters
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RandomPasswordGeneratorComposable(
    password: String,
    passwordStrengthText: String,
    color: Color,
    @DrawableRes image: Int,
    sliderValue: Float,
    copyButtonClick: () -> Unit,
    onSliderValueChange: (Float) -> Unit,
    onCheckboxClick: (Boolean, Characters) -> Unit,
    modifier: Modifier = Modifier
) {
    val passwordStrengthColor by animateColorAsState(
        targetValue = color,
        label = "password strength color"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "password Strength image"
        )

        OutlinedTextField(
            value = password,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 17.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
                focusedBorderColor = Color.Black
            ),
            readOnly = true,
            singleLine = true,
            shape = RoundedCornerShape(30),
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = passwordStrengthText,
                        modifier = Modifier
                            .background(
                                color = passwordStrengthColor,
                                shape = RoundedCornerShape(30)
                            )
                            .padding(10.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.restart),
                        contentDescription = "refresh password",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            },
            interactionSource = remember { NoRippleInteractionSource() }
        )

        Button(
            onClick = copyButtonClick,
            shape = RoundedCornerShape(30),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 15.dp,
                pressedElevation = 25.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF006FF4)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        ) { Text("Copy") }

        Text(
            text = "Password length: ${sliderValue.toInt()}"
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.minus),
                contentDescription = "increase length",
                tint = Color.Black,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(50.dp)
            )

            Slider(
                value = sliderValue,
                valueRange = 1f..50f,
                onValueChange = onSliderValueChange,
                track = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(13.dp)
                            .background(color = Color(0xFF006FF4), shape = CircleShape)
                    )
                },
                thumb = {
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 7.dp,
                                shape = CircleShape
                            )
                            .size(40.dp)
                            .background(color = Color.White, shape = CircleShape)
                            .border(
                                width = 1.dp,
                                color = Color(0xFF3C91F6),
                                shape = CircleShape
                            )
                    )
                },
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.add),
                contentDescription = "decrease length",
                tint = Color.Black,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(50.dp)
            )
        }

        Text(
            text = "Characters used:"
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                CustomCheckBox { onCheckboxClick(it, Characters.UpperCase()) }
                Text(text = "ABC", fontSize = 18.sp)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                CustomCheckBox { onCheckboxClick(it, Characters.LowerCase()) }
                Text(text = "abc", fontSize = 18.sp)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                CustomCheckBox { onCheckboxClick(it, Characters.Numbers()) }
                Text(text = "123", fontSize = 18.sp)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                CustomCheckBox { onCheckboxClick(it, Characters.Symbols()) }
                Text(text = "#$&", fontSize = 18.sp)
            }
        }
    }
}

class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = MutableSharedFlow()

    override suspend fun emit(interaction: Interaction) = Unit

    override fun tryEmit(interaction: Interaction): Boolean = false
}


@Composable
fun CustomCheckBox(
    size: Dp = 25.dp,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .size(size)
            .background(
                color = if (checked) Color.Green else Color.Gray,
                shape = RoundedCornerShape(20)
            )
            .clickable {
                checked = checked.not()
                onCheckedChange(checked)
            }
    )
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
            targetValue = 1f,
            animationSpec = tween(durationMillis = 4000, easing = LinearEasing)
        )
        isLineAnimationComplete.value = true

        // Start the checkmark animation after the line finishes
        checkmarkProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
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
                    color = Color.Red,
                    strokeWidth = 8f,
                    start = segments[i],
                    end = segments[i + 1]
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
                path = drawPath,
                color = Color.Green,
                style = Stroke(width = 8f)
            )
        }
    }
}

@Preview
@Composable
private fun RandomPasswordGeneratorComposablePrev() {
//    RandomPasswordGeneratorComposable(
//        image = R.drawable.week,
//        password = "",
//        passwordStrengthText = "",
//        color = Color.Green,
//        modifier = Modifier.background(Color.White)
//    )
}