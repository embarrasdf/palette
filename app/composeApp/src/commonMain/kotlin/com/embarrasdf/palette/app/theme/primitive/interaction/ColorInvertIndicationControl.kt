package com.embarrasdf.palette.app.theme.primitive.interaction

import androidx.compose.runtime.Stable
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.theme.primitive.IndicationTokenSet
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
class ColorInvertIndicationControl(
    private val value: () -> IndicationTokenSet.ColorInvert,
    private val onValueChange: (IndicationTokenSet.ColorInvert) -> Unit,
) {
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
        hoverAmountControl,
        pressAmountControl,
    )
}
