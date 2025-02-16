package com.example.leaflog.feature_charts.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.leaflog.ui.theme.LeafLogTheme

@Composable
fun DropDownEntry(
    modifier: Modifier = Modifier,
    valuesList: List<String>,
    value: Int,
    label: String,
    onValueChanged: (Int) -> Unit
) {
    val onSurface = Color(0xFF1D1B19)
    val onSurfaceVariant = Color(0xFF4D453E)

    var expanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier
                .clickable {
                    expanded = true
                },
            label = {
                Text(text = label)
            },
            value = valuesList[value],
            onValueChange = {},
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors().copy(
                disabledLabelColor = onSurfaceVariant,
                disabledTextColor = onSurface,
                disabledIndicatorColor = onSurface,
                disabledSuffixColor = onSurfaceVariant
            ),
            suffix = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Arrow down"
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            for (i in valuesList.indices) {
                DropdownMenuItem(
                    text = {
                        Text(text = valuesList[i])
                    },
                    onClick = {
                        onValueChanged(i)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDropDownEntry() {
    LeafLogTheme {
        Box(
            modifier = Modifier.background(color = Color.White)
        ) {
            DropDownEntry(
                valuesList = listOf("1", "2", "3"),
                value = 0,
                label = "label"
            ) {

            }
        }
    }
}