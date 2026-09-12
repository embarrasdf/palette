package com.embarrasdf.palette.components.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.testing.PaparazziTestRule
import com.embarrasdf.palette.theme.PaletteTheme
import org.junit.Rule
import org.junit.Test

class SurfaceIntrinsicTest {

    @get:Rule
    val paparazzi = PaparazziTestRule

    /**
     * Regression: a themed (rounded) Button must be measurable under IntrinsicSize without
     * throwing "Asking for intrinsic measurements of SubcomposeLayout layouts is not supported".
     * A rounded Surface previously insetted its content via a SubcomposeLayout, which has no
     * intrinsic measurements.
     */
    @Test
    fun roundedButtonUnderIntrinsicMinWidth() {
        paparazzi.snapshot {
            PaletteTheme {
                Column(modifier = Modifier.width(IntrinsicSize.Min)) {
                    Button(
                        style = PaletteTheme.component.core.button.secondary,
                        onClick = {},
                    ) {
                        Text("Configure")
                    }
                }
            }
        }
    }

    /** The rounded button should still center its content within a fixed size. */
    @Test
    fun roundedButtonFixedSizeCentersContent() {
        paparazzi.snapshot {
            PaletteTheme {
                Button(
                    style = PaletteTheme.component.core.button.secondary,
                    onClick = {},
                    modifier = Modifier.size(120.dp),
                ) {
                    Text("42")
                }
            }
        }
    }
}
