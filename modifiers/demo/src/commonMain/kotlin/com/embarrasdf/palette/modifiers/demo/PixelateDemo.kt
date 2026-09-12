package com.embarrasdf.palette.modifiers.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.modifiers.pixelate
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PixelateDemo(
    modifier: Modifier = Modifier,
) {
    val modifierState = rememberSaveable(saver = PixelateStateSaver) { PixelateState() }

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
            )
        ),
        modifier = modifier,
    )
}

private class PixelateState(
    subdivisionsInitial: Int = 0,
) {
    var subdivisions: Int by mutableStateOf(subdivisionsInitial)
}

private const val subdivisionsKey = "subdivisions"

private val PixelateStateSaver = mapSaverSafe(
    save = {
        mapOf(
            subdivisionsKey to it.subdivisions,
        )
    },
    restore = {
        PixelateState(
            subdivisionsInitial = it[subdivisionsKey] as Int,
        )
    }
)
