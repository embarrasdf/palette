package com.embarrasdf.palette.modifiers.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.state.DemoState
import com.embarrasdf.palette.components.demo.state.rememberDemoState
import com.embarrasdf.palette.modifiers.pixelate
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PixelateDemo(modifier: Modifier = Modifier) {
    val modifierState = rememberDemoState { PixelateState() }

    ModifierDemo(
        demoModifier = Modifier.pixelate(
            subdivisions = { modifierState.subdivisions },
        ),
        controls = persistentListOf(
            Control.Slider(
                name = "Subdivisions",
                value = { modifierState.subdivisions.toFloat() },
                onValueChange = {
                    modifierState.subdivisions = it.toInt()
                },
                valueRange = { 0f..100f },
            ),
        ),
        modifier = modifier,
    )
}

@Stable
private class PixelateState : DemoState() {
    var subdivisions by saveable(0)
}
