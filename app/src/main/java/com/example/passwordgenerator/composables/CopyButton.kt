package com.example.passwordgenerator.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.passwordgenerator.buttonBlueColor

private const val buttonText = "Copy"

@Composable
fun CopyButton(
    onCopyButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onCopyButtonClick,
        shape = RoundedCornerShape(30),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 15.dp,
            pressedElevation = 25.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonBlueColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) { Text(buttonText) }
}