package com.embarrasdf.palette.components.demo.subject

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.primitive.EasingPrimitiveToken
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpecType
import com.embarrasdf.palette.theme.semantic.animation.type
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * Editable state for an [AnimationSpec]. Every parameter is retained across type switches (as in
 * `GridScaleState`) so toggling between spring/tween/snap doesn't lose values.
 */
@Stable
class AnimationSpecState(
    typeInitial: AnimationSpecType = AnimationSpecType.Tween,
    dampingRatioInitial: Float = AnimationSpec.Spring().dampingRatio,
    stiffnessInitial: Float = AnimationSpec.Spring().stiffness,
    durationMillisInitial: Int = AnimationSpec.DefaultDurationMillis,
    delayMillisInitial: Int = 0,
    easingInitial: EasingPrimitiveToken = EasingPrimitiveToken.Standard,
) {
    var type by mutableStateOf(typeInitial)
        internal set
    var dampingRatio by mutableStateOf(dampingRatioInitial)
        internal set
    var stiffness by mutableStateOf(stiffnessInitial)
        internal set
    var durationMillis by mutableStateOf(durationMillisInitial)
        internal set
    var delayMillis by mutableStateOf(delayMillisInitial)
        internal set
    var easing by mutableStateOf(easingInitial)
        internal set

    val spec: AnimationSpec.Finite
        get() = when (type) {
            AnimationSpecType.Spring -> AnimationSpec.Spring(
                dampingRatio = dampingRatio,
                stiffness = stiffness,
            )

            AnimationSpecType.Tween -> AnimationSpec.Tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis,
                easing = easing,
            )

            AnimationSpecType.Snap -> AnimationSpec.Snap(
                delayMillis = delayMillis,
            )
        }

    companion object {
        /** Builds a state seeded from an existing [AnimationSpec.Finite], keeping sensible defaults
         *  for the parameters that variant does not carry. */
        fun from(spec: AnimationSpec.Finite): AnimationSpecState {
            val springDefaults = AnimationSpec.Spring()
            val tweenDefaults = AnimationSpec.Tween()
            return AnimationSpecState(
                typeInitial = spec.type(),
                dampingRatioInitial = (spec as? AnimationSpec.Spring)?.dampingRatio
                    ?: springDefaults.dampingRatio,
                stiffnessInitial = (spec as? AnimationSpec.Spring)?.stiffness
                    ?: springDefaults.stiffness,
                durationMillisInitial = (spec as? AnimationSpec.Tween)?.durationMillis
                    ?: tweenDefaults.durationMillis,
                delayMillisInitial = when (spec) {
                    is AnimationSpec.Tween -> spec.delayMillis
                    is AnimationSpec.Snap -> spec.delayMillis
                    else -> 0
                },
                easingInitial = (spec as? AnimationSpec.Tween)?.easing ?: tweenDefaults.easing,
            )
        }
    }
}

private const val typeKey = "type"
private const val dampingRatioKey = "dampingRatio"
private const val stiffnessKey = "stiffness"
private const val durationMillisKey = "durationMillis"
private const val delayMillisKey = "delayMillis"
private const val easingKey = "easing"

val AnimationSpecStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            typeKey to value.type,
            dampingRatioKey to value.dampingRatio,
            stiffnessKey to value.stiffness,
            durationMillisKey to value.durationMillis,
            delayMillisKey to value.delayMillis,
            easingKey to value.easing,
        )
    },
    restore = { value ->
        AnimationSpecState(
            typeInitial = value[typeKey] as AnimationSpecType,
            dampingRatioInitial = value[dampingRatioKey] as Float,
            stiffnessInitial = value[stiffnessKey] as Float,
            durationMillisInitial = value[durationMillisKey] as Int,
            delayMillisInitial = value[delayMillisKey] as Int,
            easingInitial = value[easingKey] as EasingPrimitiveToken,
        )
    },
)

/**
 * Exposes controls for an [AnimationSpecState]. The type selector is always shown; the remaining
 * controls are those relevant to the currently selected type (the `GridScaleControl` pattern).
 */
@Stable
class AnimationSpecControl(
    val state: AnimationSpecState,
    val onChanged: (() -> Unit)? = null,
) {
    val typeControl = enumControl(
        name = "Type",
        values = { AnimationSpecType.entries },
        selectedValue = { state.type },
        onValueChange = {
            state.type = it
            onChanged?.invoke()
        },
    )

    val dampingRatioControl = Control.Slider(
        name = "Damping ratio",
        value = { state.dampingRatio },
        onValueChange = {
            state.dampingRatio = it
            onChanged?.invoke()
        },
        valueRange = { 0.1f..1f },
    )

    val stiffnessControl = Control.Slider(
        name = "Stiffness",
        value = { state.stiffness },
        onValueChange = {
            state.stiffness = it
            onChanged?.invoke()
        },
        valueRange = { 50f..10000f },
    )

    val durationControl = Control.Slider(
        name = "Duration (ms)",
        value = { state.durationMillis.toFloat() },
        onValueChange = {
            state.durationMillis = it.toInt()
            onChanged?.invoke()
        },
        valueRange = { 0f..1000f },
        stepIncrement = 10f,
    )

    val delayControl = Control.Slider(
        name = "Delay (ms)",
        value = { state.delayMillis.toFloat() },
        onValueChange = {
            state.delayMillis = it.toInt()
            onChanged?.invoke()
        },
        valueRange = { 0f..1000f },
        stepIncrement = 10f,
    )

    val easingControl = enumControl(
        name = "Easing",
        values = { EasingPrimitiveToken.entries },
        selectedValue = { state.easing },
        onValueChange = {
            state.easing = it
            onChanged?.invoke()
        },
    )

    val controls: PersistentList<Control>
        get() = when (state.type) {
            AnimationSpecType.Spring -> persistentListOf(
                typeControl,
                dampingRatioControl,
                stiffnessControl,
            )

            AnimationSpecType.Tween -> persistentListOf(
                typeControl,
                durationControl,
                delayControl,
                easingControl,
            )

            AnimationSpecType.Snap -> persistentListOf(
                typeControl,
                delayControl,
            )
        }
}
