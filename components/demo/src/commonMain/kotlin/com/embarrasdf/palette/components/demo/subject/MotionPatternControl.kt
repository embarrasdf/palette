package com.embarrasdf.palette.components.demo.subject

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.semantic.motion.MotionPattern
import com.embarrasdf.palette.theme.semantic.motion.MotionPatternType
import com.embarrasdf.palette.theme.semantic.motion.type
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * Editable state for a [MotionPattern]. Every parameter is retained across type switches so toggling
 * between pattern types doesn't lose values.
 */
@Stable
class MotionPatternState(
    typeInitial: MotionPatternType = MotionPatternType.SharedAxis,
    fractionInitial: Float = MotionPattern.SharedAxis().fraction,
    scaleFromInitial: Float = MotionPattern.Scale().from,
) {
    var type by mutableStateOf(typeInitial)
        internal set
    var fraction by mutableStateOf(fractionInitial)
        internal set
    var scaleFrom by mutableStateOf(scaleFromInitial)
        internal set

    val pattern: MotionPattern
        get() = when (type) {
            MotionPatternType.SharedAxis -> MotionPattern.SharedAxis(fraction = fraction)
            MotionPatternType.Fade -> MotionPattern.Fade
            MotionPatternType.Scale -> MotionPattern.Scale(from = scaleFrom)
        }

    companion object {
        fun from(pattern: MotionPattern): MotionPatternState = MotionPatternState(
            typeInitial = pattern.type(),
            fractionInitial = (pattern as? MotionPattern.SharedAxis)?.fraction
                ?: MotionPattern.SharedAxis().fraction,
            scaleFromInitial = (pattern as? MotionPattern.Scale)?.from
                ?: MotionPattern.Scale().from,
        )
    }
}

private const val typeKey = "type"
private const val fractionKey = "fraction"
private const val scaleFromKey = "scaleFrom"

val MotionPatternStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            typeKey to value.type,
            fractionKey to value.fraction,
            scaleFromKey to value.scaleFrom,
        )
    },
    restore = { value ->
        MotionPatternState(
            typeInitial = value[typeKey] as MotionPatternType,
            fractionInitial = value[fractionKey] as Float,
            scaleFromInitial = value[scaleFromKey] as Float,
        )
    },
)

/**
 * Exposes controls for a [MotionPatternState]: the type selector plus the params relevant to the
 * selected type.
 */
@Stable
class MotionPatternControl(
    val state: MotionPatternState,
    val onChanged: (() -> Unit)? = null,
) {
    val typeControl = enumControl(
        name = "Pattern",
        values = { MotionPatternType.entries },
        selectedValue = { state.type },
        onValueChange = {
            state.type = it
            onChanged?.invoke()
        },
    )

    val fractionControl = Control.Slider(
        name = "Slide fraction",
        value = { state.fraction },
        onValueChange = {
            state.fraction = it
            onChanged?.invoke()
        },
        valueRange = { 0f..1f },
    )

    val scaleFromControl = Control.Slider(
        name = "Scale from",
        value = { state.scaleFrom },
        onValueChange = {
            state.scaleFrom = it
            onChanged?.invoke()
        },
        valueRange = { 0f..1f },
    )

    val controls: PersistentList<Control>
        get() = when (state.type) {
            MotionPatternType.SharedAxis -> persistentListOf(typeControl, fractionControl)
            MotionPatternType.Fade -> persistentListOf(typeControl)
            MotionPatternType.Scale -> persistentListOf(typeControl, scaleFromControl)
        }
}
