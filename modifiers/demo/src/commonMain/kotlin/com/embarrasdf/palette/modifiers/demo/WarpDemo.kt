package com.embarrasdf.palette.modifiers.demo

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.demo.ComponentDemoType
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.modifiers.warp
import kotlinx.collections.immutable.persistentListOf

@Composable
fun WarpDemo(
    modifier: Modifier = Modifier,
) {
    val modifierState = rememberSaveable(saver = WarpStateSaver) { WarpState() }
    var pointerOffset by remember { mutableStateOf(Offset.Zero) }

    ModifierDemo(
        demoModifier = Modifier
            .warp(
                offset = { pointerOffset },
                radius = { modifierState.radius },
                amount = { modifierState.amount },
            ).pointerInput(Unit) {
                detectTapGestures(
                    onPress = { pointerOffset = it },
                    onTap = { pointerOffset = it },
                )
            }.pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    pointerOffset = change.position
                }
            }
            .onSizeChanged {
                pointerOffset = Offset(it.width / 2f, it.height / 2f)
            },
        controls = persistentListOf(
            Control.Slider(
                name = "Amount",
                value = { modifierState.amount },
                onValueChange = {
                    modifierState.amount = it
                },
                valueRange = { -5f..5f },
            ),
            Control.Slider(
                name = "Radius",
                value = { modifierState.radius.value },
                onValueChange = {
                    modifierState.radius = it.dp
                },
                valueRange = { 0f..1000f }
            ),
        ),
        state = rememberModifierDemoState(componentDemoTypeInitial = ComponentDemoType.Grid),
        modifier = modifier
    )
}

private class WarpState(
    radiusInitial: Dp = 320.dp,
    amountInitial: Float = .2f,
) {
    var radius: Dp by mutableStateOf(radiusInitial)
    var amount: Float by mutableStateOf(amountInitial)
}

private const val radiusKey = "radius"
private const val amountKey = "amount"

private val WarpStateSaver = mapSaverSafe(
    save = {
        mapOf(
            radiusKey to it.radius.value,
            amountKey to it.amount,
        )
    },
    restore = {
        WarpState(
            radiusInitial = Dp(it[radiusKey] as Float),
            amountInitial = it[amountKey] as Float,
        )
    }
)
