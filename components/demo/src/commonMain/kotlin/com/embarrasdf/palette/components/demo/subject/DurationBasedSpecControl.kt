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
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/** The duration-based spec variants a [DurationBasedSpecState] can edit. */
enum class DurationBasedSpecType {
    Tween,
    Snap,
}

/**
 * Editable state for an [AnimationSpec.Finite.DurationBased] — a [AnimationSpec.Tween] or
 * [AnimationSpec.Snap]. Every parameter is retained across type switches so toggling between the
 * two does not lose values. Reused both as a stand-alone duration-based editor and as the driver of
 * a repeating spec.
 */
@Stable
class DurationBasedSpecState(
    typeInitial: DurationBasedSpecType = DurationBasedSpecType.Tween,
    durationMillisInitial: Int = AnimationSpec.DefaultDurationMillis,
    delayMillisInitial: Int = 0,
    easingInitial: EasingPrimitiveToken = EasingPrimitiveToken.Standard,
) {
    var type by mutableStateOf(typeInitial)
        internal set
    var durationMillis by mutableStateOf(durationMillisInitial)
        internal set
    var delayMillis by mutableStateOf(delayMillisInitial)
        internal set
    var easing by mutableStateOf(easingInitial)
        internal set

    val spec: AnimationSpec.Finite.DurationBased
        get() = when (type) {
            DurationBasedSpecType.Tween -> AnimationSpec.Tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis,
                easing = EasingValue.Preset(easing),
            )

            DurationBasedSpecType.Snap -> AnimationSpec.Snap(
                delayMillis = delayMillis,
            )
        }

    companion object {
        /** Builds a state seeded from an existing [AnimationSpec.Finite.DurationBased]. */
        fun from(spec: AnimationSpec.Finite.DurationBased): DurationBasedSpecState {
            val tweenDefaults = AnimationSpec.Tween()
            return DurationBasedSpecState(
                typeInitial = when (spec) {
                    is AnimationSpec.Tween -> DurationBasedSpecType.Tween
                    is AnimationSpec.Snap -> DurationBasedSpecType.Snap
                },
                durationMillisInitial = (spec as? AnimationSpec.Tween)?.durationMillis
                    ?: tweenDefaults.durationMillis,
                delayMillisInitial = when (spec) {
                    is AnimationSpec.Tween -> spec.delayMillis
                    is AnimationSpec.Snap -> spec.delayMillis
                },
                easingInitial = ((spec as? AnimationSpec.Tween)?.easing as? EasingValue.Preset)?.token
                    ?: EasingPrimitiveToken.Standard,
            )
        }
    }
}

private const val typeKey = "type"
private const val durationMillisKey = "durationMillis"
private const val delayMillisKey = "delayMillis"
private const val easingKey = "easing"

val DurationBasedSpecStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            typeKey to value.type,
            durationMillisKey to value.durationMillis,
            delayMillisKey to value.delayMillis,
            easingKey to value.easing,
        )
    },
    restore = { value ->
        DurationBasedSpecState(
            typeInitial = value[typeKey] as DurationBasedSpecType,
            durationMillisInitial = value[durationMillisKey] as Int,
            delayMillisInitial = value[delayMillisKey] as Int,
            easingInitial = value[easingKey] as EasingPrimitiveToken,
        )
    },
)

/**
 * Exposes controls for a [DurationBasedSpecState]: a type selector plus the parameters relevant to
 * the selected type (the `GridScaleControl` pattern).
 */
@Stable
class DurationBasedSpecControl(
    val state: DurationBasedSpecState,
    val onChanged: (() -> Unit)? = null,
) {
    val typeControl = enumControl(
        name = "Type",
        values = { DurationBasedSpecType.entries },
        selectedValue = { state.type },
        onValueChange = {
            state.type = it
            onChanged?.invoke()
        },
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
            DurationBasedSpecType.Tween -> persistentListOf(
                typeControl,
                durationControl,
                delayControl,
                easingControl,
            )

            DurationBasedSpecType.Snap -> persistentListOf(
                typeControl,
                delayControl,
            )
        }
}
