package com.example.passwordgenerator.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordgenerator.NoRippleInteractionSource
import com.example.passwordgenerator.R
import com.example.passwordgenerator.black

private const val iconContentDesc = "generate new password"
private const val passwordStrengthColor = "password strength color"

@Composable
fun PasswordTextField(
    password: String,
    color: Color,
    passwordStrengthText: String,
    resetPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    val passwordStrengthColor by animateColorAsState(
        targetValue = color,
        animationSpec = tween(),
        label = passwordStrengthColor
    )

    OutlinedTextField(
        value = password,
        onValueChange = {},
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        textStyle = TextStyle(
            color = black,
            fontSize = 17.sp
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = black,
            focusedBorderColor = black
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
                    contentDescription = iconContentDesc,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { resetPassword.invoke() }
                )
            }
        },
        interactionSource = remember { NoRippleInteractionSource() }
    )
}