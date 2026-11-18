package com.embarrasdf.palette.components.demo.subject

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.primitive.EasingPrimitiveToken
import com.embarrasdf.palette.theme.primitive.EasingValue
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec
import com.embarrasdf.palette.theme.semantic.animation.FiniteAnimationSpecType
import com.embarrasdf.palette.theme.semantic.animation.RepeatMode
import com.embarrasdf.palette.theme.semantic.animation.type
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

/**
 * Editable state for an [AnimationSpec.Finite]. A mechanism ([type]) is chosen among spring / tween /
 * snap, and — for the duration-based mechanisms — [repeat] wraps it in an [AnimationSpec.Repeat.Finite]
 * with [iterations] and [repeatMode]. Every parameter is retained across type switches (as in
 * `GridScaleState`) so toggling doesn't lose values.
 */
@Stable
class FiniteAnimationSpecState(
    typeInitial: FiniteAnimationSpecType = FiniteAnimationSpecType.Tween,
    dampingRatioInitial: Float = AnimationSpec.Spring().dampingRatio,
    stiffnessInitial: Float = AnimationSpec.Spring().stiffness,
    durationMillisInitial: Int = AnimationSpec.DefaultDurationMillis,
    delayMillisInitial: Int = 0,
    easingInitial: EasingPrimitiveToken = EasingPrimitiveToken.Standard,
    repeatInitial: Boolean = false,
    iterationsInitial: Int = AnimationSpec.DefaultIterations,
    repeatModeInitial: RepeatMode = RepeatMode.Restart,
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
    var repeat by mutableStateOf(repeatInitial)
        internal set
    var iterations by mutableStateOf(iterationsInitial)
        internal set
    var repeatMode by mutableStateOf(repeatModeInitial)
        internal set

    /** The chosen mechanism, before any repeat wrapping. */
    private val mechanism: AnimationSpec.Finite
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
        }

    val spec: AnimationSpec.Finite
        get() {
            val mechanism = mechanism
            return if (repeat && mechanism is AnimationSpec.DurationBased) {
                AnimationSpec.Repeat.Finite(
                    animation = mechanism,
                    iterations = iterations,
                    repeatMode = repeatMode,
                )
            } else {
                mechanism
            }
        }

    companion object {
        /** Builds a state seeded from an existing [AnimationSpec.Finite], unwrapping a repeat into
         *  its mechanism and keeping sensible defaults for the parameters that variant does not carry. */
        fun from(spec: AnimationSpec.Finite): FiniteAnimationSpecState {
            val springDefaults = AnimationSpec.Spring()
            val tweenDefaults = AnimationSpec.Tween()
            val repeat = spec as? AnimationSpec.Repeat.Finite
            val mechanism: AnimationSpec.Finite = repeat?.animation ?: spec
            return FiniteAnimationSpecState(
                typeInitial = mechanism.type(),
                dampingRatioInitial = (mechanism as? AnimationSpec.Spring)?.dampingRatio
                    ?: springDefaults.dampingRatio,
                stiffnessInitial = (mechanism as? AnimationSpec.Spring)?.stiffness
                    ?: springDefaults.stiffness,
                durationMillisInitial = (mechanism as? AnimationSpec.Tween)?.durationMillis
                    ?: tweenDefaults.durationMillis,
                delayMillisInitial = when (mechanism) {
                    is AnimationSpec.Tween -> mechanism.delayMillis
                    is AnimationSpec.Snap -> mechanism.delayMillis
                    else -> 0
                },
                easingInitial = ((mechanism as? AnimationSpec.Tween)?.easing as? EasingValue.Preset)?.token
                    ?: EasingPrimitiveToken.Standard,
                repeatInitial = repeat != null,
                iterationsInitial = repeat?.iterations ?: AnimationSpec.DefaultIterations,
                repeatModeInitial = repeat?.repeatMode ?: RepeatMode.Restart,
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
private const val repeatKey = "repeat"
private const val iterationsKey = "iterations"
private const val repeatModeKey = "repeatMode"

val FiniteAnimationSpecStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            typeKey to value.type,
            dampingRatioKey to value.dampingRatio,
            stiffnessKey to value.stiffness,
            durationMillisKey to value.durationMillis,
            delayMillisKey to value.delayMillis,
            easingKey to value.easing,
            repeatKey to value.repeat,
            iterationsKey to value.iterations,
            repeatModeKey to value.repeatMode,
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
            repeatInitial = value[repeatKey] as Boolean,
            iterationsInitial = value[iterationsKey] as Int,
            repeatModeInitial = value[repeatModeKey] as RepeatMode,
        )
    },
)

/**
 * Exposes controls for an [FiniteAnimationSpecState]. The type selector is always shown, followed by
 * the controls relevant to the selected mechanism (the `GridScaleControl` pattern). For the
 * duration-based mechanisms a "Repeat" toggle is shown last; enabling it reveals an indented column
 * with the [AnimationSpec.Repeat.Finite] controls (spring can't be repeated, so no toggle there).
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

    val repeatControl = Control.Toggle(
        name = "Repeat",
        value = { state.repeat },
        onValueChange = {
            state.repeat = it
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

    private val repeatColumn = Control.ControlColumn(
        indent = true,
        expandedInitial = true,
        controls = { persistentListOf(iterationsControl, repeatModeControl) },
    )

    val controls: PersistentList<Control>
        get() = buildList {
            add(typeControl)
            when (state.type) {
                FiniteAnimationSpecType.Spring -> {
                    add(dampingRatioControl)
                    add(stiffnessControl)
                }

                FiniteAnimationSpecType.Tween -> {
                    add(durationControl)
                    add(delayControl)
                    add(easingControl)
                }

                FiniteAnimationSpecType.Snap -> {
                    add(delayControl)
                }
            }
            // Only duration-based mechanisms can be repeated.
            if (state.type != FiniteAnimationSpecType.Spring) {
                add(repeatControl)
                if (state.repeat) {
                    add(repeatColumn)
                }
            }
        }.toPersistentList()
}
