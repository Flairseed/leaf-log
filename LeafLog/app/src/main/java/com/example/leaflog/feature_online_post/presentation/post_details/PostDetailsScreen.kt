package com.example.leaflog.feature_online_post.presentation.post_details

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.leaflog.feature_log.presentation.component.LogPage
import com.example.leaflog.ui.theme.schoolBellFamily

@Composable
fun PostDetailsScreen(
    title: String,
    description: String,
    picture: String,
    height: Float,
    water: Int,
    temperature: Int?,
    relativeHumidity: Int?,
    lightLevel: Float?,
    created: Long,
    goBack: () -> Unit
) {
    val surface = Color(0xFFFFF8F5)

    val columnScroll = rememberScrollState()
    val titleScroll = rememberScrollState()
    val descriptionScroll = rememberScrollState()

    Scaffold(
        topBar = {
            Box(modifier = Modifier.height(80.dp)) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = goBack
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "")
                }
            }
        },
        containerColor = surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .consumeWindowInsets(it)
                .verticalScroll(columnScroll),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogPage {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 49.dp,
                            top = 17.dp
                        )
                        .horizontalScroll(titleScroll),
                    text = title,
                    fontSize = 36.sp,
                    fontFamily = schoolBellFamily,
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .padding(
                            top = 80.dp,
                            start = 116.dp
                        )
                        .width(136.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .border(
                                width = 1.dp,
                                color = Color.Black
                            )
                            .padding(
                                top = 13.dp,
                                start = 13.dp,
                                end = 13.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .fillMaxWidth()
                                .background(Color.Black),
                            model = picture,
                            contentDescription = ""
                        )
                        Text(
                            text = DateFormat.format("dd/MM/yyyy", created).toString(),
                            fontSize = 20.sp,
                            fontFamily = schoolBellFamily
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(
                            top = 247.dp,
                            start = 55.dp
                        )
                ) {
                    Text(
                        text = "Height: $height cm",
                        fontFamily = schoolBellFamily,
                        fontSize = 15.sp
                    )
                    Text(
                        modifier = Modifier.padding(top = 28.dp),
                        text = "Water: $water ml",
                        fontFamily = schoolBellFamily,
                        fontSize = 15.sp
                    )
                    Text(
                        modifier = Modifier.padding(top = 28.dp),
                        text = "Light: ${lightLevel ?: "?"} lx",
                        fontFamily = schoolBellFamily,
                        fontSize = 15.sp
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(
                            top = 247.dp,
                            end = 16.dp
                        ),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Temperature: ${temperature ?: "?"} °C",
                        fontFamily = schoolBellFamily,
                        fontSize = 15.sp
                    )
                    Text(
                        modifier = Modifier.padding(top = 28.dp),
                        text = "R. Humidity: ${relativeHumidity ?: "?"} %",
                        fontFamily = schoolBellFamily,
                        fontSize = 15.sp
                    )
                }
            }

            LogPage(
                modifier = Modifier.padding(
                    vertical = 30.dp
                )
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 17.dp, start = 35.dp),
                    text = "Log",
                    fontFamily = schoolBellFamily,
                    fontSize = 36.sp
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                        .padding(
                            top = 85.dp,
                            start = 54.dp,
                            end = 6.dp
                        )
                        .verticalScroll(descriptionScroll),
                    text = description,
                    lineHeight = 26.sp,
                    fontFamily = schoolBellFamily,
                )
            }
        }
    }
}