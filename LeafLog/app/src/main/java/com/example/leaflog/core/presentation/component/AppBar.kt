package com.example.leaflog.core.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.leaflog.ui.theme.LeafLogTheme

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    label: String,
    onProfileClicked: () -> Unit,
) {
    val background = Color(0xFF2E5B00)
    val foreground = Color(0xFFFFFFFF)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = onProfileClicked
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = "",
                tint = foreground
            )
        }
        Text(
            text = label,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            color = foreground
        )
    }
}

@Preview
@Composable
private fun PreviewAppBar() {
    LeafLogTheme {
        AppBar(
            label = "Journals",
            onProfileClicked = {}
        )
    }
}