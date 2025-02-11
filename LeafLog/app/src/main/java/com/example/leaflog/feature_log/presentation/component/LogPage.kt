package com.example.leaflog.feature_log.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.leaflog.R
import com.example.leaflog.ui.theme.LeafLogTheme

@Composable
fun LogPage(
    modifier: Modifier = Modifier,
    content: @Composable() (BoxScope.() -> Unit)
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(321.dp)
                .height(399.dp)
        ) {
            Image(
                modifier = Modifier
                    .matchParentSize()
                    .shadow(elevation = 5.dp)
                ,
                imageVector = ImageVector.vectorResource(id = R.drawable.log),
                contentDescription = "Log Page",
            )
            content()
        }
    }
}

@Preview
@Composable
private fun PreviewLogPage() {
    LeafLogTheme {
        LogPage() {}
    }
}