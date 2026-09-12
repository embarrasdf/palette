package com.embarrasdf.palette.app.theme.primitive.interaction

import androidx.compose.runtime.Stable
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.modifiers.NoiseColorMode
import com.embarrasdf.palette.theme.primitive.IndicationTokenSet
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
class NoiseIndicationControl(
    private val value: () -> IndicationTokenSet.Noise,
    private val onValueChange: (IndicationTokenSet.Noise) -> Unit,
) {
    private val colorModeControl = enumControl(
        name = "Color mode",
        values = { NoiseColorMode.entries },
        selectedValue = { value().colorMode },
        onValueChange = { onValueChange(value().copy(colorMode = it)) },
    )

    private val hoverAmountControl = Control.Slider(
        name = "Hover amount",
        value = { value().hoverAmount },
        onValueChange = { onValueChange(value().copy(hoverAmount = it)) },
        valueRange = { 0f..1f },
    )

    private val pressAmountControl = Control.Slider(
        name = "Press amount",
        value = { value().pressAmount },
        onValueChange = { onValueChange(value().copy(pressAmount = it)) },
        valueRange = { 0f..1f },
    )

    val controls: PersistentList<Control> = persistentListOf(
        colorModeControl,
        hoverAmountControl,
        pressAmountControl,
    )
}
