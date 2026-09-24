package com.embarrasdf.palette.components.demo.subject

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.semantic.animation.AnimationToken
import com.embarrasdf.palette.theme.semantic.motion.Axis
import com.embarrasdf.palette.theme.semantic.motion.Transition
import com.embarrasdf.palette.theme.semantic.motion.TransitionType
import com.embarrasdf.palette.theme.semantic.motion.type
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * Editable state for a [Transition]. Every parameter is retained across type switches so toggling
 * between transition types doesn't lose values.
 */
@Stable
class TransitionState(
    typeInitial: TransitionType = TransitionType.SharedAxis,
    axisInitial: Axis = Axis.X,
    fractionInitial: Float = Transition.SharedAxis().fraction,
    scaleFromInitial: Float = Transition.Scale().from,
    animationInitial: AnimationToken = AnimationToken.Default,
) {
    var type by mutableStateOf(typeInitial)
        internal set
    var axis by mutableStateOf(axisInitial)
        internal set
    var fraction by mutableStateOf(fractionInitial)
        internal set
    var scaleFrom by mutableStateOf(scaleFromInitial)
        internal set
    var animation by mutableStateOf(animationInitial)
        internal set

    val transition: Transition
        get() = when (type) {
            TransitionType.SharedAxis -> Transition.SharedAxis(axis = axis, fraction = fraction, animation = animation)
            TransitionType.Fade -> Transition.Fade(animation = animation)
            TransitionType.Scale -> Transition.Scale(from = scaleFrom, animation = animation)
            TransitionType.None -> Transition.None
        }

    companion object {
        fun from(transition: Transition): TransitionState = TransitionState(
            typeInitial = transition.type(),
            axisInitial = (transition as? Transition.SharedAxis)?.axis ?: Axis.X,
            fractionInitial = (transition as? Transition.SharedAxis)?.fraction
                ?: Transition.SharedAxis().fraction,
            scaleFromInitial = (transition as? Transition.Scale)?.from ?: Transition.Scale().from,
            animationInitial = transition.animationTokenOrDefault(),
        )
    }
}

private fun Transition.animationTokenOrDefault(): AnimationToken = when (this) {
    is Transition.SharedAxis -> animation
    is Transition.Fade -> animation
    is Transition.Scale -> animation
    Transition.None -> AnimationToken.Default
}

private const val typeKey = "type"
private const val axisKey = "axis"
private const val fractionKey = "fraction"
private const val scaleFromKey = "scaleFrom"
private const val animationKey = "animation"

val TransitionStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            typeKey to value.type,
            axisKey to value.axis,
            fractionKey to value.fraction,
            scaleFromKey to value.scaleFrom,
            animationKey to value.animation,
        )
    },
    restore = { value ->
        TransitionState(
            typeInitial = value[typeKey] as TransitionType,
            axisInitial = value[axisKey] as Axis,
            fractionInitial = value[fractionKey] as Float,
            scaleFromInitial = value[scaleFromKey] as Float,
            animationInitial = value[animationKey] as AnimationToken,
        )
    },
)

/**
 * Exposes controls for a [TransitionState]: the animation reference, the transition type, and the
 * params relevant to the selected type.
 */
@Stable
class TransitionControl(
    val state: TransitionState,
    val onChanged: (() -> Unit)? = null,
) {
    val animationControl = enumControl(
        name = "Animation",
        values = { AnimationToken.entries },
        selectedValue = { state.animation },
        onValueChange = {
            state.animation = it
            onChanged?.invoke()
        },
    )

    val typeControl = enumControl(
        name = "Transition",
        values = { TransitionType.entries },
        selectedValue = { state.type },
        onValueChange = {
            state.type = it
            onChanged?.invoke()
        },
    )

    val axisControl = enumControl(
        name = "Axis",
        values = { Axis.entries },
        selectedValue = { state.axis },
        onValueChange = {
            state.axis = it
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
            TransitionType.SharedAxis -> persistentListOf(animationControl, typeControl, axisControl, fractionControl)
            TransitionType.Fade -> persistentListOf(animationControl, typeControl)
            TransitionType.Scale -> persistentListOf(animationControl, typeControl, scaleFromControl)
            TransitionType.None -> persistentListOf(animationControl, typeControl)
        }
}
