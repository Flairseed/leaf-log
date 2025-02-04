package com.example.leaflog.feature_journal.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.leaflog.R

@Composable
fun Book(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    image: String,
) {
    val foreground = 0xFF881A1A
    val text = Color.White
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(158.dp)
                .height(202.dp),
        ) {
            Image(
                modifier = Modifier.matchParentSize(),
                imageVector = ImageVector.vectorResource(id = R.drawable.journal),
                contentDescription = "Journal book",
            )
            AsyncImage(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 30.dp)
                    .size(70.dp)
                    .background(
                        Color(foreground),
                        shape = MaterialTheme.shapes.small,
                    )
                    .padding(5.dp)
                    .clip(shape = MaterialTheme.shapes.small)
                    .background(Color.Black),
                model = image,
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .padding(top = 106.dp)
                    .background(Color(foreground))
                    .width(155.dp),
                text = title,
                color = text,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Box(
                modifier = Modifier
                    .padding(top = 140.dp)
                    .background(Color(foreground))
                    .width(155.dp)
                    .height(55.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.Center),
                    text = description,
                    color = text,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewBook() {
    Book(
        title = "Sunflower",
        description = "My journal for sunflower",
        image = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/Sunflower_sky_backdrop.jpg/800px-Sunflower_sky_backdrop.jpg"
    )
}