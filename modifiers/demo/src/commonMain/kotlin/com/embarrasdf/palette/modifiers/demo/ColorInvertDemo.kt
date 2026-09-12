package com.embarrasdf.palette.modifiers.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.modifiers.colorInvert
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ColorInvertDemo(
    modifier: Modifier = Modifier,
) {
    val modifierState = rememberSaveable(saver = ColorInvertStateSaver) { ColorInvertState() }

    ModifierDemo(
        demoModifier = Modifier.colorInvert(
            amount = { modifierState.amount },
        ),
        controls = persistentListOf(
            Control.Slider(
                name = "Amount",
                value = { modifierState.amount },
                onValueChange = {
                    modifierState.amount = it
                },
                valueRange = { 0f..1f },
            )
        ),
        modifier = modifier,
    )
}

private class ColorInvertState(
    amountInitial: Float = 0f,
) {
    var amount: Float by mutableStateOf(amountInitial)
}

private const val amountKey = "amount"

private val ColorInvertStateSaver = mapSaverSafe(
    save = {
        mapOf(
            amountKey to it.amount,
        )
    },
    restore = {
        ColorInvertState(
            amountInitial = it[amountKey] as Float,
        )
    }
)

