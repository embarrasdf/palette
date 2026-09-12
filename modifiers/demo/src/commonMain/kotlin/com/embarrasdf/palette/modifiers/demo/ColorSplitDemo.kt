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
import com.embarrasdf.palette.modifiers.ColorSplitMode
import com.embarrasdf.palette.modifiers.colorSplit
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ColorSplitDemo(
    modifier: Modifier = Modifier,
) {
    val modifierState = rememberSaveable(saver = ColorSplitStateSaver) { ColorSplitState() }

    ModifierDemo(
        demoModifier = Modifier.colorSplit(
            xAmount = { modifierState.xAmount },
            yAmount = { modifierState.yAmount },
            colorMode = { modifierState.colorMode },
        ),
        controls = persistentListOf(
            enumControl(
                name = "Color mode",
                values = { ColorSplitMode.entries },
                selectedValue = { modifierState.colorMode },
                onValueChange = { colorMode ->
                    modifierState.colorMode = colorMode
                },
            ),
            Control.Slider(
                name = "X Amount",
                value = { modifierState.xAmount },
                onValueChange = {
                    modifierState.xAmount = it
                },
                valueRange = { -1f..1f },
            ),
            Control.Slider(
                name = "Y Amount",
                value = { modifierState.yAmount },
                onValueChange = {
                    modifierState.yAmount = it
                },
                valueRange = { -1f..1f },
            ),
        ),
        modifier = modifier,
    )
}

private class ColorSplitState(
    xAmountInitial: Float = 0f,
    yAmountInitial: Float = 0f,
    colorModeInitial: ColorSplitMode = ColorSplitMode.RGB,
) {
    var xAmount: Float by mutableStateOf(xAmountInitial)
    var yAmount: Float by mutableStateOf(yAmountInitial)
    var colorMode: ColorSplitMode by mutableStateOf(colorModeInitial)
}

private const val xAmountKey = "xAmount"
private const val yAmountKey = "yAmount"
private const val colorModeKey = "colorMode"

private val ColorSplitStateSaver = mapSaverSafe(
    save = {
        mapOf(
            xAmountKey to it.xAmount,
            yAmountKey to it.yAmount,
            colorModeKey to it.colorMode.ordinal,
        )
    },
    restore = {
        ColorSplitState(
            xAmountInitial = it[xAmountKey] as Float,
            yAmountInitial = it[yAmountKey] as Float,
            colorModeInitial = ColorSplitMode.entries[(it[colorModeKey] as Int)],
        )
    }
)
