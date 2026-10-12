package com.embarrasdf.palette.components.geometry

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Surface
import kotlin.math.PI

@Preview
@Composable
private fun CartesianGridPreview() {
    Surface {
        CartesianGrid(
            xSpacing = { 20.dp.toPx() },
            ySpacing = { 20.dp.toPx() },
            lineStyle = GridLineStyle(
                color = Color(0xFF6200EE),
                stroke = Stroke(width = 1f),
            ),
            modifier = Modifier.size(200.dp),
            offset = Offset(0f, 10f),
        )
    }
}

@Preview
@Composable
private fun CartesianGridLogarithmicScalePreview() {
    Surface {
        Grid(
            coordinateSystem = GridCoordinateSystem.Cartesian(
                scaleX = GridScale.Logarithmic(spacing = 1.dp, base = 2f),
                scaleY = GridScale.Logarithmic(spacing = 1.dp, base = 2f),
            ),
            lineStyle = GridLineStyle(
                color = Color(0xFF6200EE),
                stroke = Stroke(width = 1f),
            ),
            modifier = Modifier.size(200.dp),
        )
    }
}

@Preview
@Composable
private fun CartesianGridLogarithmicDecayScalePreview() {
    Surface {
        Grid(
            coordinateSystem = GridCoordinateSystem.Cartesian(
                scaleX = GridScale.LogarithmicDecay(spacing = 50.dp, base = 2f),
                scaleY = GridScale.LogarithmicDecay(spacing = 50.dp, base = 2f),
            ),
            lineStyle = GridLineStyle(
                color = Color(0xFF6200EE),
                stroke = Stroke(width = 1f),
            ),
            modifier = Modifier.size(200.dp),
        )
    }
}

@Preview
@Composable
private fun CartesianGridExponentialScalePreview() {
    Surface {
        Grid(
            coordinateSystem = GridCoordinateSystem.Cartesian(
                scaleX = GridScale.Exponential(spacing = 1.dp, exponent = 2f),
                scaleY = GridScale.Exponential(spacing = 1.dp, exponent = 2f),
            ),
            lineStyle = GridLineStyle(
                color = Color(0xFF6200EE),
                stroke = Stroke(width = 1f),
            ),
            modifier = Modifier.size(200.dp),
        )
    }
}

@Preview
@Composable
private fun CartesianGridExponentialDecayScalePreview() {
    Surface {
        Grid(
            coordinateSystem = GridCoordinateSystem.Cartesian(
                scaleX = GridScale.ExponentialDecay(spacing = 100.dp, exponent = 2f),
                scaleY = GridScale.ExponentialDecay(spacing = 100.dp, exponent = 2f),
            ),
            lineStyle = GridLineStyle(
                color = Color(0xFF6200EE),
                stroke = Stroke(width = 1f),
            ),
            modifier = Modifier.size(200.dp),
        )
    }
}

@Preview
@Composable
private fun PolarGridPreview() {
    Surface {
        PolarGrid(
            radiusSpacing = { 30.dp.toPx() },
            theta = { (PI / 3).toFloat() },
            lineStyle = GridLineStyle(
                color = Color(0xFF6200EE),
                stroke = Stroke(width = 1f),
            ),
            modifier = Modifier.size(200.dp),
        )
    }
}

@Preview
@Composable
private fun OvalGridPreview() {
    val coordinateSystem = GridCoordinateSystem.Cartesian(spacing = 20.dp)
    Surface {
        Grid(
            coordinateSystem = coordinateSystem,
            lineStyle = null,
            vertex = GridVertex.Oval(
                color = Color(0xFF6200EE),
                size = DpSize(4.dp / 2f, 4.dp / 2f),
                drawStyle = Stroke(width = 1f),
            ),
            modifier = Modifier.size(200.dp),
            offset = Offset(80f, 10f),
        )
    }
}

@Preview
@Composable
private fun RectGridPreview() {
    val coordinateSystem = GridCoordinateSystem.Cartesian(spacing = 20.dp)
    Surface {
        Grid(
            coordinateSystem = coordinateSystem,
            lineStyle = null,
            vertex = GridVertex.Rect(
                color = Color(0xFF6200EE),
                size = DpSize(8.dp, 8.dp),
                drawStyle = Stroke(width = 1f),
            ),
            modifier = Modifier.size(200.dp),
            offset = Offset(80f, 10f),
        )
    }
}

@Preview
@Composable
private fun PlusGridPreview() {
    val coordinateSystem = GridCoordinateSystem.Cartesian(spacing = 20.dp)
    Surface {
        Grid(
            coordinateSystem = coordinateSystem,
            lineStyle = null,
            vertex = GridVertex.Plus(
                color = Color(0xFF6200EE),
                size = DpSize(8.dp, 8.dp),
                strokeWidth = Dp.Hairline,
            ),
            modifier = Modifier.size(200.dp),
            offset = Offset(80f, 0f),
        )
    }
}
