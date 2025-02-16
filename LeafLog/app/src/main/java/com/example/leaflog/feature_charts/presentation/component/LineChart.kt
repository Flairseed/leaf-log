package com.example.leaflog.feature_charts.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.leaflog.ui.theme.LeafLogTheme

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    maxCanvasHeight: Dp = 270.dp,
    title: String,
    verticalAxis: String,
    horizontalAxis: String,
    relative: Boolean = false
) {
    val surfaceContainer = Color(0xFFF3EDE9)
    val onSurface = Color(0xFF1D1B19)
    val primary = Color(0xFF2E5B00)
    val primaryContainer = Color(0xFF4C821C)

    val maxValue = values.max()
    val minValue = values.min()

    val arrowWidth = 7.dp

    val textMeasurer = rememberTextMeasurer()
    val verticalAxisScroll = rememberScrollState()
    val horizontalAxisScroll = rememberScrollState()
    val titleScrollState = rememberScrollState()

    val finalValues = if (values.size > 7) {
        values.subList(0, 7)
    } else {
        values
    }

    Box(
        modifier = modifier
            .background(surfaceContainer)
            .border(
                1.dp,
                color = onSurface
            )
    ) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(maxCanvasHeight)
                .background(surfaceContainer)
                .padding(
                    top = 30.dp,
                    start = 20.dp,
                    bottom = 20.dp,
                    end = 20.dp
                ),
            contentDescription = "Line Chart"
        ) {
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
            // Line chart elements
            val leftOffset = 7.dp.toPx()
            val bottomOffset = 7.dp.toPx()
            val distBetweenPoints = (size.width - 4 * leftOffset) / 6
            val maxPlotHeight = size.height - 100
            for (i in finalValues.indices) {
                val value = finalValues[i]
                val y = size.height - (if (maxValue != 0f) (if (relative) (if (value != minValue) ((value - minValue) / (maxValue - minValue)) else 0f) else (value / maxValue)) else 0f) * maxPlotHeight - bottomOffset
                val x = i * distBetweenPoints + leftOffset
                // Lines
                if (i < finalValues.size - 1) {
                    val value2 = finalValues[i + 1]
                    val y2 = size.height - (if (maxValue != 0f) (if (relative) (if (value != minValue) ((value - minValue) / (maxValue - minValue)) else 0f) else (value2 / maxValue)) else 0f) * maxPlotHeight - bottomOffset
                    val x2 = (i + 1) * distBetweenPoints + leftOffset
                    drawLine(
                        color = primaryContainer,
                        start = Offset(x, y),
                        end = Offset(x2, y2),
                        strokeWidth = 3.dp.toPx()
                    )
                }
                // Plot values
                drawText(
                    textMeasurer = textMeasurer,
                    text = value.toString(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = onSurface,
                        background = surfaceContainer
                    ),
                    topLeft = Offset(x+15f, y-11f)
                )
                // Plots
                drawCircle(
                    color = primary,
                    radius = 10f,
                    center = Offset(x, y)
                )
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
                .width(maxCanvasHeight - 100.dp)
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
        LineChart(
            values = listOf(0f, 1.5f, 2.2f, 3.0f, 4.6f),
            title = "Height of plant over time",
            horizontalAxis = "Log",
            verticalAxis = "Height (cm)"
        )
    }
}