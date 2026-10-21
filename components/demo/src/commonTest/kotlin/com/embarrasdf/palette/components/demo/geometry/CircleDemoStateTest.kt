package com.embarrasdf.palette.components.demo.geometry

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.demo.util.saveAndRestore
import kotlin.test.Test
import kotlin.test.assertEquals

class CircleDemoStateTest {

    private val density = Density(density = 2f)

    @Test
    fun restoresEveryProperty() {
        val state = CircleDemoState(density = density, colorInitial = Color.Red).apply {
            color = Color.Blue
            strokeWidth = 8.dp
            drawStyle = Stroke(width = strokeWidthPx)
        }

        val restored = circleDemoStateSaver(density = density, color = Color.Red).saveAndRestore(state)

        assertEquals(Color.Blue, restored.color)
        assertEquals(8.dp, restored.strokeWidth)
        assertEquals(Stroke(width = 16f), restored.drawStyle)
    }
}
