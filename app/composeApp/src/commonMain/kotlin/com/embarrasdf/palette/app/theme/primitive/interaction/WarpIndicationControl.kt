package com.embarrasdf.palette.app.theme.primitive.interaction

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.theme.primitive.IndicationTokenSet
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
class WarpIndicationControl(
    private val value: () -> IndicationTokenSet.Warp,
    private val onValueChange: (IndicationTokenSet.Warp) -> Unit,
) {
    private val pressAmountControl = Control.Slider(
        name = "Press amount",
        value = { value().pressAmount },
        onValueChange = { onValueChange(value().copy(pressAmount = it)) },
        valueRange = { -5f..5f },
    )

    private val pressRadiusControl = Control.Slider(
        name = "Press radius",
        value = { value().pressRadius.value },
        onValueChange = { onValueChange(value().copy(pressRadius = it.dp)) },
        valueRange = { 0f..1000f },
    )

    val controls: PersistentList<Control> = persistentListOf(
        pressAmountControl,
        pressRadiusControl,
    )
}
