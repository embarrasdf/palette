package com.embarrasdf.palette.modifiers.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.modifiers.NoiseColorMode
import com.embarrasdf.palette.modifiers.noise
import kotlinx.collections.immutable.persistentListOf

@Composable
fun NoiseDemo(
    modifier: Modifier = Modifier,
) {
    val modifierState = rememberSaveable(saver = NoiseStateSaver) { NoiseState() }

    ModifierDemo(
        demoModifier = Modifier.noise(
            amount = { modifierState.amount },
            colorMode = modifierState.colorMode,
        ),
        controls = persistentListOf(
            Control.Slider(
                name = "Amount",
                value = { modifierState.amount },
                onValueChange = {
                    modifierState.amount = it
                },
                valueRange = { 0f..1f },
            ),
            enumControl(
                name = "Color Mode",
                values = { NoiseColorMode.entries },
                selectedValue = { modifierState.colorMode },
                onValueChange = { colorMode ->
                    modifierState.colorMode = colorMode
                },
            ),
        ),
        modifier = modifier,
    )
}

private class NoiseState(
    amountInitial: Float = 0f,
    colorModeInitial: NoiseColorMode = NoiseColorMode.Monochrome,
) {
    var amount: Float by mutableStateOf(amountInitial)
    var colorMode: NoiseColorMode by mutableStateOf(colorModeInitial)
}

private const val amountKey = "amount"
private const val colorModeKey = "colorMode"

private val NoiseStateSaver = mapSaverSafe(
    save = {
        mapOf(
            colorModeKey to it.colorMode.ordinal,
            amountKey to it.amount,
        )
    },
    restore = {
        NoiseState(
            colorModeInitial = NoiseColorMode.entries[(it[colorModeKey] as Int)],
            amountInitial = it[amountKey] as Float,
        )
    }
)

