package com.example.leaflog.feature_journal.presentation.journal_details

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.leaflog.core.presentation.component.CustomButton

@Composable
fun JournalDetailsScreen(
    journalId: Int,
    title: String,
    description: String,
    picture: String,
    goBack: () -> Unit,
    onEdit: (Int) -> Unit,
    goToLogs: (Int) -> Unit,
) {
    val background = Color(0xFFF77171)
    val textColor = Color.White
    val textBackground = Color(0xFF881A1A)
    val surfaceContainer = Color(0xFFF3EDE9)
    val onSurface = Color(0xFF1D1B19)

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
        containerColor = background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .consumeWindowInsets(it)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier
                    .padding(bottom = 40.dp)
                    .width(250.dp)
                    .background(textBackground, RoundedCornerShape(10.dp))
                    .aspectRatio(1f)
                    .padding(20.dp)
                ) {
                    Box(modifier = Modifier
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .fillMaxSize()
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black),
                            model = picture,
                            contentDescription = ""
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp)
                        .background(textBackground)
                        .padding(10.dp)
                        .horizontalScroll(titleScroll),
                    text = title,
                    fontSize = 24.sp,
                    color = textColor,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(textBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(10.dp)
                            .verticalScroll(descriptionScroll),
                        text = description,
                        fontSize = 16.sp,
                        color = textColor,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                CustomButton(
                    modifier = Modifier.width(150.dp),
                    label = "Edit",
                    leadingIcon = Icons.Default.Create
                ) {
                    onEdit(journalId)
                }
                CustomButton(
                    modifier = Modifier.width(150.dp),
                    label = "Go to Logs",
                    leadingIcon = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    color = surfaceContainer,
                    foreground = onSurface
                ) {
                    goToLogs(journalId)
                }
            }
        }
    }
}