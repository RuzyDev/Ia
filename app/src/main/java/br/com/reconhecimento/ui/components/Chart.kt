package br.com.reconhecimento.ui.components

import androidx.compose.ui.graphics.Path
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import br.com.reconhecimento.util.formatCasasDecimais

@Composable
fun LinearChart(
    modifier: Modifier = Modifier,
    style: LinearChartStyle = LinearChartStyle.Default,
    data: List<Double>
) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {
            if (data.size > 2) {
                Column(
                    Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Divider(Modifier.width(4.dp))
                    Text(
                        text = data.max().formatCasasDecimais(2),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(vertical = (0.5).dp)
                    )
                    Divider(
                        Modifier
                            .weight(1f)
                            .width(1.dp))
                    Text(
                        text = (data.max() / 2).formatCasasDecimais(2),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(vertical = (0.5).dp)
                    )
                    Divider(
                        Modifier
                            .weight(1f)
                            .width(1.dp))
                    Text(
                        text = data.min().formatCasasDecimais(2),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(vertical = (0.5).dp)
                    )
                    Divider(Modifier.width(4.dp))
                }
            }

            Canvas(modifier = Modifier
                .fillMaxHeight()
                .weight(1f)) {
                // distance between each x point
                val distance = size.width / (data.size + 1)
                var currentX = 0F
                val maxValue = data.maxOrNull() ?: 0.0
                val points = mutableListOf<PointF>()

                data.forEachIndexed { index, currentData ->
                    if (data.size >= index + 2) {
                        val y0 = (maxValue - currentData) * (size.height / maxValue)
                        val x0 = currentX + distance
                        points.add(PointF(x0, y0.toFloat()))
                        currentX += distance
                    }
                }

                if (style == LinearChartStyle.Default) {
                    for (i in 0 until points.size - 1) {
                        drawLine(
                            start = Offset(points[i].x, points[i].y),
                            end = Offset(points[i + 1].x, points[i + 1].y),
                            color = Color(255, 0, 0),
                            strokeWidth = 8f
                        )
                    }
                } else {
                    val cubicPoints1 = mutableListOf<PointF>()
                    val cubicPoints2 = mutableListOf<PointF>()

                    for (i in 1 until points.size) {
                        cubicPoints1.add(
                            PointF(
                                (points[i].x + points[i - 1].x) / 2,
                                points[i - 1].y
                            )
                        )
                        cubicPoints2.add(PointF((points[i].x + points[i - 1].x) / 2, points[i].y))
                    }

                    val path = Path()
                    path.moveTo(points.first().x, points.first().y)

                    for (i in 1 until points.size) {
                        path.cubicTo(
                            cubicPoints1[i - 1].x,
                            cubicPoints1[i - 1].y,
                            cubicPoints2[i - 1].x,
                            cubicPoints2[i - 1].y,
                            points[i].x,
                            points[i].y
                        )
                    }

                    drawPath(path, color = Color(255, 0, 0), style = Stroke(width = 8f))
                }
            }
        }
    }
}

sealed class LinearChartStyle {
    object Default : LinearChartStyle()
    object Smooth : LinearChartStyle()
}