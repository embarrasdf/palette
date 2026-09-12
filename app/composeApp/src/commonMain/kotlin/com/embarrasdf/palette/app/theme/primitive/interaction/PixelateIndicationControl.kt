package com.embarrasdf.palette.app.theme.primitive.interaction

import androidx.compose.runtime.Stable
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.theme.primitive.IndicationTokenSet
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
class PixelateIndicationControl(
    private val value: () -> IndicationTokenSet.Pixelate,
    private val onValueChange: (IndicationTokenSet.Pixelate) -> Unit,
) {
    private val hoverSubdivisionsControl = Control.Slider(
        name = "Hover subdivisions",
        value = { value().hoverSubdivisions.toFloat() },
        onValueChange = { onValueChange(value().copy(hoverSubdivisions = it.toInt())) },
        valueRange = { 0f..100f },
    )

    private val pressSubdivisionsControl = Control.Slider(
        name = "Press subdivisions",
        value = { value().pressSubdivisions.toFloat() },
        onValueChange = { onValueChange(value().copy(pressSubdivisions = it.toInt())) },
        valueRange = { 0f..100f },
    )

    val controls: PersistentList<Control> = persistentListOf(
        hoverSubdivisionsControl,
        pressSubdivisionsControl,
    )
}
