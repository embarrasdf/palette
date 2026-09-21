package com.embarrasdf.palette.theme.semantic.animation

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.Spring as ComposeSpring
import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.primitive.EasingPrimitiveToken

/**
 * A time-driven, target-based animation: a value evolving over time toward a target. This is
 * Palette's application-neutral counterpart to Compose's animation spec.
 *
 * The hierarchy mirrors the platform's own capability boundaries, and each boundary is a constraint
 * some consumer relies on:
 *
 * - [Finite] — the spec terminates. One-shot animations (screen transitions, demo subjects) require
 *   a [Finite] spec so the value actually settles.
 * - [Finite.DurationBased] — a [Finite] spec with an explicit duration. Only these may be wrapped
 *   by a repeat (you cannot repeat a spring), so a future `Repeatable` would take a [DurationBased].
 * - [Infinite] — the spec never terminates (loops / ambient motion). No variants yet.
 *
 * Velocity-based, target-less specs (fling / friction) are intentionally *not* part of this
 * hierarchy; see [DecaySpec].
 */
sealed interface AnimationSpec {

    /** Specs that terminate. Transitions and other one-shot animations require this. */
    sealed interface Finite : AnimationSpec {
        /** Finite specs with an explicit duration; only these may be wrapped by a repeat. */
        sealed interface DurationBased : Finite
    }

    /** Specs that never terminate — loops / ambient motion. No variants yet. */
    sealed interface Infinite : AnimationSpec

    data class Tween(
        val durationMillis: Int = DefaultDurationMillis,
        val delayMillis: Int = 0,
        val easing: EasingPrimitiveToken = EasingPrimitiveToken.Standard,
    ) : Finite.DurationBased

    data class Snap(
        val delayMillis: Int = 0,
    ) : Finite.DurationBased

    data class Spring(
        val dampingRatio: Float = ComposeSpring.DampingRatioNoBouncy,
        val stiffness: Float = ComposeSpring.StiffnessMedium,
    ) : Finite

    companion object {
        const val DefaultDurationMillis: Int = 300
    }
}

enum class AnimationSpecType {
    Spring,
    Tween,
    Snap,
}

fun AnimationSpec.Finite.type(): AnimationSpecType = when (this) {
    is AnimationSpec.Spring -> AnimationSpecType.Spring
    is AnimationSpec.Tween -> AnimationSpecType.Tween
    is AnimationSpec.Snap -> AnimationSpecType.Snap
}

/**
 * Resolves this finite spec to a Compose [FiniteAnimationSpec], resolving a [AnimationSpec.Tween]'s
 * easing through the given primitive map (mirroring `IndicationToken.toIndication(scheme, primitives)`).
 */
fun <T> AnimationSpec.Finite.toComposeSpec(
    easings: Map<EasingPrimitiveToken, Easing>,
): FiniteAnimationSpec<T> = when (this) {
    is AnimationSpec.Spring -> spring(dampingRatio = dampingRatio, stiffness = stiffness)
    is AnimationSpec.Tween -> tween(
        durationMillis = durationMillis,
        delayMillis = delayMillis,
        easing = easings.getValue(easing),
    )
    is AnimationSpec.Snap -> snap(delayMillis = delayMillis)
}

@Composable
fun <T> AnimationSpec.Finite.toComposeSpec(): FiniteAnimationSpec<T> {
    return toComposeSpec(PaletteTheme.primitive.easing)
}
