package com.embarrasdf.palette.components.geometry

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.testing.PaparazziTestRule
import com.embarrasdf.palette.theme.PaletteTheme
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class CurveStitchTest(
    @TestParameter(valuesProvider = NumLinesProvider::class)
    private val numLines: Int,
    @TestParameter(valuesProvider = NumPointsProvider::class)
    private val numPoints: Int,
) {

    @get:Rule
    val paparazzi = PaparazziTestRule

    object NumLinesProvider : TestParameter.TestParameterValuesProvider {
        override fun provideValues() = listOf(2, 4, 8, 16, 32)
    }

    object NumPointsProvider : TestParameter.TestParameterValuesProvider {
        override fun provideValues() = listOf(3, 5, 7, 9)
    }

    @Test
    fun curveStitch() {
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    CurveStitch(
                        start = Offset(0.1f, 0.1f),
                        vertex = Offset(0.1f, 0.9f),
                        end = Offset(0.9f, 0.9f),
                        numLines = numLines,
                        strokeWidth = Dp.Hairline,
                        color = PaletteTheme.semantic.color.primary,
                        modifier = Modifier.size(200.dp),
                    )
                }
            }
        }
    }

    @Test
    fun curveStitchStar() {
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    CurveStitchStar(
                        numLines = numLines,
                        numPoints = numPoints,
                        strokeWidth = Dp.Hairline,
                        color = PaletteTheme.semantic.color.primary,
                        innerRadius = 0.5f,
                        drawInsidePoints = true,
                        drawOutsidePoints = true,
                        modifier = Modifier.size(200.dp),
                    )
                }
            }
        }
    }

    @Test
    fun curveStitchStarInsideOnly() {
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    CurveStitchStar(
                        numLines = numLines,
                        numPoints = numPoints,
                        strokeWidth = Dp.Hairline,
                        color = PaletteTheme.semantic.color.primary,
                        innerRadius = 0.5f,
                        drawOutsidePoints = false,
                        modifier = Modifier.size(200.dp),
                    )
                }
            }
        }
    }

    @Test
    fun curveStitchStarOutsideOnly() {
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    CurveStitchStar(
                        numLines = numLines,
                        numPoints = numPoints,
                        strokeWidth = Dp.Hairline,
                        color = PaletteTheme.semantic.color.primary,
                        innerRadius = 0.5f,
                        drawInsidePoints = true,
                        modifier = Modifier.size(200.dp),
                    )
                }
            }
        }
    }

    @Test
    fun curveStitchStarInnerRadius0() {
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    CurveStitchStar(
                        numLines = numLines,
                        numPoints = numPoints,
                        strokeWidth = Dp.Hairline,
                        color = PaletteTheme.semantic.color.primary,
                        innerRadius = 0f,
                        modifier = Modifier.size(200.dp),
                    )
                }
            }
        }
    }

    @Test
    fun curveStitchShape() {
        paparazzi.snapshot {
            PaletteTheme {
                Surface(style = PaletteTheme.component.core.surface.default) {
                    CurveStitchShape(
                        numLines = numLines,
                        numPoints = numPoints,
                        strokeWidth = Dp.Hairline,
                        color = PaletteTheme.semantic.color.primary,
                        modifier = Modifier.size(200.dp),
                    )
                }
            }
        }
    }
}
