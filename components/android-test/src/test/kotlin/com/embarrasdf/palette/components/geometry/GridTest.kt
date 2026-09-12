package com.embarrasdf.palette.components.geometry

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.testing.PaparazziTestRule
import com.embarrasdf.palette.theme.PaletteTheme
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.PI

@RunWith(TestParameterInjector::class)
class GridTest {
    @get:Rule
    val paparazzi = PaparazziTestRule

    @Test
    fun cartesianGrid() {
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    CartesianGrid(
                        xSpacing = { 20.dp.toPx() },
                        ySpacing = { 20.dp.toPx() },
                        lineStyle = GridLineStyle(
                            color = PaletteTheme.semantic.color.primary,
                            stroke = Stroke(width = 1f),
                        ),
                        modifier = Modifier.size(200.dp),
                        offset = Offset(0f, 10f),
                    )
                }
            }
        }
    }

    @Test
    fun cartesianGridLogarithmicScale() {
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    Grid(
                        coordinateSystem = GridCoordinateSystem.Cartesian(
                            scaleX = GridScale.Logarithmic(spacing = 1.dp, base = 2f),
                            scaleY = GridScale.Logarithmic(spacing = 1.dp, base = 2f),
                        ),
                        lineStyle = GridLineStyle(
                            color = PaletteTheme.semantic.color.primary,
                            stroke = Stroke(width = 1f),
                        ),
                        modifier = Modifier.size(200.dp),
                    )
                }
            }
        }
    }

    @Test
    fun cartesianGridLogarithmicDecayScale() {
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    Grid(
                        coordinateSystem = GridCoordinateSystem.Cartesian(
                            scaleX = GridScale.LogarithmicDecay(spacing = 50.dp, base = 2f),
                            scaleY = GridScale.LogarithmicDecay(spacing = 50.dp, base = 2f),
                        ),
                        lineStyle = GridLineStyle(
                            color = PaletteTheme.semantic.color.primary,
                            stroke = Stroke(width = 1f),
                        ),
                        modifier = Modifier.size(200.dp),
                    )
                }
            }
        }
    }

    @Test
    fun cartesianGridExponentialScale() {
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    Grid(
                        coordinateSystem = GridCoordinateSystem.Cartesian(
                            scaleX = GridScale.Exponential(spacing = 1.dp, exponent = 2f),
                            scaleY = GridScale.Exponential(spacing = 1.dp, exponent = 2f),
                        ),
                        lineStyle = GridLineStyle(
                            color = PaletteTheme.semantic.color.primary,
                            stroke = Stroke(width = 1f),
                        ),
                        modifier = Modifier.size(200.dp),
                    )
                }
            }
        }
    }

    @Test
    fun cartesianGridExponentialDecayScale() {
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    Grid(
                        coordinateSystem = GridCoordinateSystem.Cartesian(
                            scaleX = GridScale.ExponentialDecay(spacing = 100.dp, exponent = 2f),
                            scaleY = GridScale.ExponentialDecay(spacing = 100.dp, exponent = 2f),
                        ),
                        lineStyle = GridLineStyle(
                            color = PaletteTheme.semantic.color.primary,
                            stroke = Stroke(width = 1f),
                        ),
                        modifier = Modifier.size(200.dp),
                    )
                }
            }
        }
    }

    @Test
    fun polarGrid() {
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    PolarGrid(
                        radiusSpacing = { 30.dp.toPx() },
                        theta = { (PI / 3).toFloat() },
                        lineStyle = GridLineStyle(
                            color = PaletteTheme.semantic.color.primary,
                            stroke = Stroke(width = 1f),
                        ),
                        modifier = Modifier.size(200.dp),
                    )
                }
            }
        }
    }

    @Test
    fun ovalGrid() {
        val coordinateSystem = GridCoordinateSystem.Cartesian(spacing = 20.dp)
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    Grid(
                        coordinateSystem = coordinateSystem,
                        lineStyle = null,
                        vertex = GridVertex.Oval(
                            color = PaletteTheme.semantic.color.primary,
                            size = DpSize(
                                PaletteTheme.semantic.dimension.spacing.xs / 2f,
                                PaletteTheme.semantic.dimension.spacing.xs / 2f
                            ),
                            drawStyle = Stroke(width = 1f),
                        ),
                        modifier = Modifier.size(200.dp),
                        offset = Offset(80f, 10f),
                    )
                }
            }
        }
    }

    @Test
    fun rectGrid() {
        val coordinateSystem = GridCoordinateSystem.Cartesian(spacing = 20.dp)
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    Grid(
                        coordinateSystem = coordinateSystem,
                        lineStyle = null,
                        vertex = GridVertex.Rect(
                            color = PaletteTheme.semantic.color.primary,
                            size = DpSize(
                                PaletteTheme.semantic.dimension.spacing.small,
                                PaletteTheme.semantic.dimension.spacing.small
                            ),
                            drawStyle = Stroke(width = 1f),
                        ),
                        modifier = Modifier.size(200.dp),
                        offset = Offset(80f, 10f),
                    )
                }
            }
        }
    }

    @Test
    fun plusGrid() {
        val coordinateSystem = GridCoordinateSystem.Cartesian(spacing = 20.dp)
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    Grid(
                        coordinateSystem = coordinateSystem,
                        lineStyle = null,
                        vertex = GridVertex.Plus(
                            color = PaletteTheme.semantic.color.primary,
                            size = DpSize(
                                PaletteTheme.semantic.dimension.spacing.small,
                                PaletteTheme.semantic.dimension.spacing.small
                            ),
                            strokeWidth = Dp.Hairline,
                        ),
                        modifier = Modifier.size(200.dp),
                        offset = Offset(80f, 0f),
                    )
                }
            }
        }
    }
}
