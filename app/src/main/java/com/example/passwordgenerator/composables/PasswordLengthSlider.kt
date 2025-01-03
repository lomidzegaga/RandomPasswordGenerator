package com.example.passwordgenerator.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.passwordgenerator.R
import com.example.passwordgenerator.buttonBlueColor
import com.example.passwordgenerator.lightBlue
import com.example.passwordgenerator.white

private const val increaseIconContentDesc = "increase length"
private const val decreaseIconContentDesc = "decrease length"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordLengthSlider(
    sliderValue: Float,
    onSliderValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.minus),
            contentDescription = increaseIconContentDesc,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .size(50.dp)
                .clickable { if (sliderValue.toInt() > 1) onSliderValueChange(sliderValue - 1f) }
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
                        .background(color = buttonBlueColor, shape = CircleShape)
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
                        .background(color = white, shape = CircleShape)
                        .border(
                            width = 1.dp,
                            color = lightBlue,
                            shape = CircleShape
                        )
                )
            },
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.add),
            contentDescription = decreaseIconContentDesc,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .size(50.dp)
                .clickable { if (sliderValue.toInt() < 50) onSliderValueChange(sliderValue + 1f) }
        )
    }
}