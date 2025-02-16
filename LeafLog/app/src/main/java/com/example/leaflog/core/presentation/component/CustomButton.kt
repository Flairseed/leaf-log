package com.example.leaflog.core.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.leaflog.ui.theme.LeafLogTheme

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    label: String,
    color: Color = Color(0xFF2E5B00),
    foreground: Color = Color.White,
    leadingIcon: ImageVector? = null,
    onClick: () -> Unit,
) {
    ElevatedButton(
        modifier = modifier,
        colors = ButtonDefaults.elevatedButtonColors().copy(
            containerColor = color,
            contentColor = foreground,
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(18.dp),
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = leadingIcon,
                    contentDescription = ""
                )
            }
            Text(text = label)
        }
    }
}

@Preview
@Composable
private fun PreviewCustomButton() {
    LeafLogTheme {
        CustomButton(
            modifier = Modifier.width(250.dp),
            label = "Confirm",
            leadingIcon = Icons.Outlined.Create,
        ) {

        }
    }
}

