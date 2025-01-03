package com.example.passwordgenerator.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.passwordgenerator.buttonBlueColor
import com.example.passwordgenerator.gray

@Composable
fun CheckBox(
    size: Dp = 25.dp,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(true) }
    Box(
        modifier = modifier
            .size(size)
            .background(
                color = if (checked) buttonBlueColor else gray,
                shape = RoundedCornerShape(20)
            )
            .clickable {
                checked = checked.not()
                onCheckedChange(checked)
            }
    )
}