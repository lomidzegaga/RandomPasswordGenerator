package com.example.passwordgenerator.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordgenerator.Characters

private const val upperCase = "ABC"
private const val lowerCase = "abc"
private const val numbers = "123"
private const val symbols = "#\$&"

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Characters(
    onCheckboxClick: (Characters, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CheckBox { onCheckboxClick(Characters.UpperCase(), it) }
            Text(text = upperCase, fontSize = 18.sp)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            CheckBox { onCheckboxClick(Characters.LowerCase(), it) }
            Text(text = lowerCase, fontSize = 18.sp)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CheckBox { onCheckboxClick(Characters.Numbers(), it) }
            Text(text = numbers, fontSize = 18.sp)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CheckBox { onCheckboxClick(Characters.Symbols(), it) }
            Text(text = symbols, fontSize = 18.sp)
        }
    }
}