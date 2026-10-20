package com.embarrasdf.palette.components.demo.subject

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.primitive.EasingPrimitiveToken
import com.embarrasdf.palette.theme.primitive.EasingValue
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec
import com.embarrasdf.palette.theme.semantic.animation.FiniteAnimationSpecType
import com.embarrasdf.palette.theme.semantic.animation.RepeatMode
import com.embarrasdf.palette.theme.semantic.animation.type
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * Editable state for an [AnimationSpec.Finite]. Every parameter is retained across type switches (as
 * in `GridScaleState`) so toggling between spring/tween/snap/repeatable doesn't lose values. A
 * [FiniteAnimationSpecType.Repeatable] is edited as an iteration count and repeat mode over a nested
 * [driverState], its duration-based driver.
 */
@Stable
class FiniteAnimationSpecState(
    typeInitial: FiniteAnimationSpecType = FiniteAnimationSpecType.Tween,
    dampingRatioInitial: Float = AnimationSpec.Spring().dampingRatio,
    stiffnessInitial: Float = AnimationSpec.Spring().stiffness,
    durationMillisInitial: Int = AnimationSpec.DefaultDurationMillis,
    delayMillisInitial: Int = 0,
    easingInitial: EasingPrimitiveToken = EasingPrimitiveToken.Standard,
    iterationsInitial: Int = AnimationSpec.DefaultIterations,
    repeatModeInitial: RepeatMode = RepeatMode.Restart,
    val driverState: DurationBasedSpecState = DurationBasedSpecState(),
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
    var iterations by mutableStateOf(iterationsInitial)
        internal set
    var repeatMode by mutableStateOf(repeatModeInitial)
        internal set

    val spec: AnimationSpec.Finite
        get() = when (type) {
            FiniteAnimationSpecType.Spring -> AnimationSpec.Spring(
                dampingRatio = dampingRatio,
                stiffness = stiffness,
            )

            FiniteAnimationSpecType.Tween -> AnimationSpec.Tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis,
                easing = EasingValue.Preset(easing),
            )

            FiniteAnimationSpecType.Snap -> AnimationSpec.Snap(
                delayMillis = delayMillis,
            )

            FiniteAnimationSpecType.Repeatable -> AnimationSpec.Repeatable(
                iterations = iterations,
                animation = driverState.spec,
                repeatMode = repeatMode,
            )
        }

    companion object {
        /** Builds a state seeded from an existing [AnimationSpec.Finite], keeping sensible defaults
         *  for the parameters that variant does not carry. */
        fun from(spec: AnimationSpec.Finite): FiniteAnimationSpecState {
            val springDefaults = AnimationSpec.Spring()
            val tweenDefaults = AnimationSpec.Tween()
            val repeatable = spec as? AnimationSpec.Repeatable
            return FiniteAnimationSpecState(
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
                easingInitial = ((spec as? AnimationSpec.Tween)?.easing as? EasingValue.Preset)?.token
                    ?: EasingPrimitiveToken.Standard,
                iterationsInitial = repeatable?.iterations ?: AnimationSpec.DefaultIterations,
                repeatModeInitial = repeatable?.repeatMode ?: RepeatMode.Restart,
                driverState = repeatable?.let { DurationBasedSpecState.from(it.animation) }
                    ?: DurationBasedSpecState(),
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
private const val iterationsKey = "iterations"
private const val repeatModeKey = "repeatMode"
private const val driverKey = "driver"

val FiniteAnimationSpecStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            typeKey to value.type,
            dampingRatioKey to value.dampingRatio,
            stiffnessKey to value.stiffness,
            durationMillisKey to value.durationMillis,
            delayMillisKey to value.delayMillis,
            easingKey to value.easing,
            iterationsKey to value.iterations,
            repeatModeKey to value.repeatMode,
            driverKey to save(value.driverState, DurationBasedSpecStateSaver, this),
        )
    },
    restore = { value ->
        FiniteAnimationSpecState(
            typeInitial = value[typeKey] as FiniteAnimationSpecType,
            dampingRatioInitial = value[dampingRatioKey] as Float,
            stiffnessInitial = value[stiffnessKey] as Float,
            durationMillisInitial = value[durationMillisKey] as Int,
            delayMillisInitial = value[delayMillisKey] as Int,
            easingInitial = value[easingKey] as EasingPrimitiveToken,
            iterationsInitial = value[iterationsKey] as Int,
            repeatModeInitial = value[repeatModeKey] as RepeatMode,
            driverState = restore(value[driverKey], DurationBasedSpecStateSaver)!!,
        )
    },
)

/**
 * Exposes controls for an [FiniteAnimationSpecState]. The type selector is always shown; the remaining
 * controls are those relevant to the currently selected type (the `GridScaleControl` pattern).
 */
@Stable
class FiniteAnimationSpecControl(
    val state: FiniteAnimationSpecState,
    val onChanged: (() -> Unit)? = null,
) {
    val typeControl = enumControl(
        name = "Type",
        values = { FiniteAnimationSpecType.entries },
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

    val iterationsControl = Control.Slider(
        name = "Iterations",
        value = { state.iterations.toFloat() },
        onValueChange = {
            state.iterations = it.toInt()
            onChanged?.invoke()
        },
        valueRange = { 2f..10f },
        stepIncrement = 1f,
    )

    val repeatModeControl = enumControl(
        name = "Repeat mode",
        values = { RepeatMode.entries },
        selectedValue = { state.repeatMode },
        onValueChange = {
            state.repeatMode = it
            onChanged?.invoke()
        },
    )

    /** The nested duration-based driver repeated by a [FiniteAnimationSpecType.Repeatable]. */
    private val driverControl = DurationBasedSpecControl(state.driverState, onChanged)

    private val driverColumn = Control.ControlColumn(
        name = "Animation",
        expandedInitial = true,
        indent = true,
        controls = { driverControl.controls },
    )

    val controls: PersistentList<Control>
        get() = when (state.type) {
            FiniteAnimationSpecType.Spring -> persistentListOf(
                typeControl,
                dampingRatioControl,
                stiffnessControl,
            )

            FiniteAnimationSpecType.Tween -> persistentListOf(
                typeControl,
                durationControl,
                delayControl,
                easingControl,
            )

            FiniteAnimationSpecType.Snap -> persistentListOf(
                typeControl,
                delayControl,
            )

            FiniteAnimationSpecType.Repeatable -> persistentListOf(
                typeControl,
                iterationsControl,
                repeatModeControl,
                driverColumn,
            )
        }
}
