package com.example.leaflog.feature_log.presentation.component

import android.text.format.DateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.leaflog.R
import com.example.leaflog.ui.theme.LeafLogTheme
import com.example.leaflog.ui.theme.schoolBellFamily
import java.time.LocalDate
import java.util.Date

@Composable
fun LogSimple(
    modifier: Modifier = Modifier,
    title: String,
    picture: String,
    created: Date,
    author: String
    ) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(156.dp)
                .height(194.dp)
        ) {
            Image(
                modifier = Modifier.matchParentSize(),
                imageVector = ImageVector.vectorResource(id = R.drawable.log),
                contentDescription = "Log Page",
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 6.dp, start = 23.dp),
                text = title,
                fontSize = 20.sp,
                fontFamily = schoolBellFamily,
                textAlign = TextAlign.Center
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 42.dp, start = 50.dp)
                    .background(Color.White)
                    .border(width = 1.dp, color = Color.Black)
                    .padding(
                        top = 8.dp,
                        start = 8.dp,
                        end = 8.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    modifier = Modifier
                        .background(Color.Black)
                        .border(width = 1.dp, color = Color.Black)
                        .size(64.dp),
                    model = picture,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = DateFormat.format("dd/MM/yyyy", created).toString(),
                    fontSize = 12.sp,
                    fontFamily = schoolBellFamily
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 18.dp, start = 28.dp),
                text = "By: $author",
                fontSize = 10.sp,
                fontFamily = schoolBellFamily,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLogSimple() {
    LeafLogTheme {
        LogSimple(
            title = "Day 1",
            picture = "",
            created = Date(),
            author = "John Doe"
        )
    }
}