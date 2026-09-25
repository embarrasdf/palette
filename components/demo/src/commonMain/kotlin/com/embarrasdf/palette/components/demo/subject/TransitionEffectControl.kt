package com.embarrasdf.palette.components.demo.subject

import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.theme.semantic.animation.AnimationToken
import com.embarrasdf.palette.theme.semantic.motion.Axis
import com.embarrasdf.palette.theme.semantic.motion.TransitionEffect
import com.embarrasdf.palette.theme.semantic.motion.TransitionEffectType
import com.embarrasdf.palette.theme.semantic.motion.type
import kotlinx.collections.immutable.toPersistentList

/** The [TransitionEffect] used when a new effect is added to a transition. */
fun defaultTransitionEffect(): TransitionEffect = TransitionEffect.Fade()

/** Switches an effect's type, carrying its animation reference to the new type's defaults. */
private fun TransitionEffect.withType(type: TransitionEffectType): TransitionEffect = when (type) {
    TransitionEffectType.Fade -> TransitionEffect.Fade(animation = animation)
    TransitionEffectType.Scale -> TransitionEffect.Scale(animation = animation)
    TransitionEffectType.Translate -> TransitionEffect.Translate(animation = animation)
}

private fun TransitionEffect.withAnimation(token: AnimationToken): TransitionEffect = when (this) {
    is TransitionEffect.Fade -> copy(animation = token)
    is TransitionEffect.Scale -> copy(animation = token)
    is TransitionEffect.Translate -> copy(animation = token)
}

/**
 * A "Transition" [Control.ControlColumn] that edits a single [TransitionEffect]: its type, the
 * value-animation preset that times it, and the params relevant to the selected type. Emits a new
 * effect through [onChange] on every edit.
 */
fun transitionEffectControl(
    effect: TransitionEffect,
    onChange: (TransitionEffect) -> Unit,
): Control.ControlColumn {
    val typeControl = enumControl(
        name = "Effect",
        values = { TransitionEffectType.entries },
        selectedValue = { effect.type() },
        onValueChange = { onChange(effect.withType(it)) },
    )

    val animationControl = enumControl(
        name = "Animation",
        values = { AnimationToken.entries },
        selectedValue = { effect.animation },
        onValueChange = { onChange(effect.withAnimation(it)) },
    )

    return Control.ControlColumn(
        name = "Transition",
        expandedInitial = true,
        indent = false,
        controls = {
            buildList<Control> {
                add(typeControl)
                add(animationControl)
                when (effect) {
                    is TransitionEffect.Fade -> Unit
                    is TransitionEffect.Scale -> {
                        add(
                            Control.Slider(
                                name = "Scale from",
                                value = { effect.from },
                                onValueChange = { onChange(effect.copy(from = it)) },
                                valueRange = { 0f..1f },
                            )
                        )
                        add(
                            Control.Slider(
                                name = "Scale to",
                                value = { effect.to },
                                onValueChange = { onChange(effect.copy(to = it)) },
                                valueRange = { 0f..1f },
                            )
                        )
                    }
                    is TransitionEffect.Translate -> {
                        add(
                            enumControl(
                                name = "Axis",
                                values = { Axis.entries },
                                selectedValue = { effect.axis },
                                onValueChange = { onChange(effect.copy(axis = it)) },
                            )
                        )
                        add(
                            Control.Slider(
                                name = "Translate fraction",
                                value = { effect.fraction },
                                onValueChange = { onChange(effect.copy(fraction = it)) },
                                valueRange = { 0f..1f },
                            )
                        )
                    }
                }
            }.toPersistentList()
        },
    )
}
