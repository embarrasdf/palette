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
import com.embarrasdf.palette.theme.semantic.animation.RepeatMode
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * Editable state for an [AnimationSpec.Infinite]. Models a [AnimationSpec.Repeatable] over a
 * [AnimationSpec.Tween] driver — the useful infinite case — exposing its duration, easing, and
 * repeat mode.
 */
@Stable
class InfiniteAnimationSpecState(
    durationMillisInitial: Int = AnimationSpec.Tween().durationMillis,
    easingInitial: EasingPrimitiveToken = EasingPrimitiveToken.Standard,
    repeatModeInitial: RepeatMode = RepeatMode.Restart,
) {
    var durationMillis by mutableStateOf(durationMillisInitial)
        internal set
    var easing by mutableStateOf(easingInitial)
        internal set
    var repeatMode by mutableStateOf(repeatModeInitial)
        internal set

    val spec: AnimationSpec.Infinite
        get() = AnimationSpec.Repeatable(
            animation = AnimationSpec.Tween(durationMillis = durationMillis, easing = easing),
            repeatMode = repeatMode,
        )

    companion object {
        /** Builds a state seeded from an existing [AnimationSpec.Infinite]. */
        fun from(spec: AnimationSpec.Infinite): InfiniteAnimationSpecState {
            val repeatable = spec as? AnimationSpec.Repeatable
            val tween = repeatable?.animation as? AnimationSpec.Tween
            val tweenDefaults = AnimationSpec.Tween()
            return InfiniteAnimationSpecState(
                durationMillisInitial = tween?.durationMillis ?: tweenDefaults.durationMillis,
                easingInitial = tween?.easing ?: tweenDefaults.easing,
                repeatModeInitial = repeatable?.repeatMode ?: RepeatMode.Restart,
            )
        }
    }
}

private const val durationMillisKey = "durationMillis"
private const val easingKey = "easing"
private const val repeatModeKey = "repeatMode"

val InfiniteAnimationSpecStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            durationMillisKey to value.durationMillis,
            easingKey to value.easing,
            repeatModeKey to value.repeatMode,
        )
    },
    restore = { value ->
        InfiniteAnimationSpecState(
            durationMillisInitial = value[durationMillisKey] as Int,
            easingInitial = value[easingKey] as EasingPrimitiveToken,
            repeatModeInitial = value[repeatModeKey] as RepeatMode,
        )
    },
)

/** Exposes controls for an [InfiniteAnimationSpecState]: duration, easing, and repeat mode. */
@Stable
class InfiniteAnimationSpecControl(
    val state: InfiniteAnimationSpecState,
    val onChanged: (() -> Unit)? = null,
) {
    val durationControl = Control.Slider(
        name = "Duration (ms)",
        value = { state.durationMillis.toFloat() },
        onValueChange = {
            state.durationMillis = it.toInt()
            onChanged?.invoke()
        },
        valueRange = { 0f..3000f },
        stepIncrement = 50f,
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

    val repeatModeControl = enumControl(
        name = "Repeat mode",
        values = { RepeatMode.entries },
        selectedValue = { state.repeatMode },
        onValueChange = {
            state.repeatMode = it
            onChanged?.invoke()
        },
    )

    val controls: PersistentList<Control> = persistentListOf(
        durationControl,
        easingControl,
        repeatModeControl,
    )
}
