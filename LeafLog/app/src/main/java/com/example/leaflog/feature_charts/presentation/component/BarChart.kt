package com.example.leaflog.feature_charts.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.leaflog.ui.theme.LeafLogTheme

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    maxBarHeight: Dp = 200.dp,
    title: String,
    verticalAxis: String,
    horizontalAxis: String
) {
    val surfaceContainer = Color(0xFFF3EDE9)
    val onSurface = Color(0xFF1D1B19)
    val primary = Color(0xFF2E5B00)

    val maxValue = values.max()
    val maxHeight = maxBarHeight + 70.dp

    val arrowWidth = 7.dp

    val verticalAxisScroll = rememberScrollState()
    val horizontalAxisScroll = rememberScrollState()
    val titleScrollState = rememberScrollState()

    Box(
        modifier = modifier
            .background(surfaceContainer)
            .border(
                1.dp,
                color = onSurface
            )
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight)
                .background(surfaceContainer)
                .padding(
                    top = 30.dp,
                    start = 20.dp,
                    bottom = 20.dp,
                    end = 20.dp
                )
                .drawBehind {
                    // X-axis line
                    drawLine(
                        color = onSurface,
                        start = Offset(0f + arrowWidth.toPx(), size.height - arrowWidth.toPx()),
                        end = Offset(size.width, size.height - arrowWidth.toPx()),
                        strokeWidth = 3.dp.toPx()
                    )
                    // X-axis arrow lines
                    drawLine(
                        color = onSurface,
                        start = Offset(
                            size.width - arrowWidth.toPx(),
                            size.height - 2 * arrowWidth.toPx()
                        ),
                        end = Offset(size.width, size.height - arrowWidth.toPx() + 1f),
                        strokeWidth = 3.dp.toPx()

                    )
                    drawLine(
                        color = onSurface,
                        start = Offset(size.width, size.height - arrowWidth.toPx() - 1f),
                        end = Offset(size.width - arrowWidth.toPx(), size.height),
                        strokeWidth = 3.dp.toPx()

                    )
                    // Y-axis line
                    drawLine(
                        color = onSurface,
                        start = Offset(arrowWidth.toPx(), 0f),
                        end = Offset(arrowWidth.toPx(), size.height - (arrowWidth - 1.dp).toPx()),
                        strokeWidth = 3.dp.toPx()
                    )
                    // Y-axis arrow lines
                    drawLine(
                        color = onSurface,
                        start = Offset(0f, 0f + arrowWidth.toPx()),
                        end = Offset(0f + arrowWidth.toPx() + 1f, 0f),
                        strokeWidth = 3.dp.toPx()

                    )
                    drawLine(
                        color = onSurface,
                        start = Offset(0f + arrowWidth.toPx() - 1f, 0f),
                        end = Offset(0f + 2 * arrowWidth.toPx(), 0f + arrowWidth.toPx()),
                        strokeWidth = 3.dp.toPx()

                    )
                }
                .padding(start = 12.dp, bottom = 8.5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            val finalValues = if (values.size > 7) {
                values.subList(0, 7)
            } else {
                values
            }

            for (i in 0..6) {
                val value = if (finalValues.size >= i + 1) {
                    finalValues[i]
                } else {
                    null
                }

                val height = (value?.div(maxValue) ?: 0f) * maxBarHeight.value

                Column(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .height(maxBarHeight)
                        .weight(1f),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = value?.toString() ?: "",
                        fontSize = 12.sp
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height.dp)
                            .background(primary)
                    )
                }
            }
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .horizontalScroll(titleScrollState),
            text = title,
            color = onSurface,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .horizontalScroll(horizontalAxisScroll),
            text = horizontalAxis,
            color = onSurface,
            fontSize = 14.sp,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.height, placeable.width) {
                        placeable.place(
                            x = -(placeable.width / 2 - placeable.height / 2),
                            y = -(placeable.height / 2 - placeable.width / 2)
                        )
                    }
                }
                .align(Alignment.CenterStart)
                .width(maxHeight - 100.dp)
                .rotate(-90f)
                .horizontalScroll(verticalAxisScroll),
            text = verticalAxis,
            color = onSurface,
            maxLines = 1,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            softWrap = false
        )
    }
}

@Preview
@Composable
private fun PreviewBarChart() {
    LeafLogTheme {
        BarChart(
            values = listOf(50f, 20f, 60f, 70f),
            title = "Water given over time",
            horizontalAxis = "Log",
            verticalAxis = "Water (ml)"
        )
    }
}