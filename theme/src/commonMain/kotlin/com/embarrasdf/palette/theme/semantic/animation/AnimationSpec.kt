package com.embarrasdf.palette.theme.semantic.animation

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.primitive.EasingPrimitiveToken
import com.embarrasdf.palette.theme.primitive.EasingValue
import com.embarrasdf.palette.theme.primitive.toEasing
import androidx.compose.animation.core.RepeatMode as ComposeRepeatMode
import androidx.compose.animation.core.Spring as ComposeSpring

/**
 * A time-driven, target-based animation: a value evolving over time toward a target. This is
 * Palette's application-neutral counterpart to Compose's animation spec.
 *
 * Two mechanisms generate motion:
 *
 * - [DurationBased][Finite.DurationBased] — a curve sampled over an explicit duration ([Tween],
 *   [Snap]). Only these can be repeated, since a repeat wraps time around a known cycle length.
 * - physics — a spring simulation ([Spring]) that settles on thresholds, with no fixed duration.
 *
 * [Finite] crosscuts them as the "settles" marker that one-shot consumers (transitions) require.
 * Looping is a combinator, not a mechanism: [Repeatable] repeats a [DurationBased] spec forever, so
 * it is the one spec that is not [Finite]. Velocity-based, target-less specs (fling / friction) are
 * intentionally *not* part of this hierarchy; see [DecaySpec].
 */
sealed interface AnimationSpec {

    /** Specs that settle. Transitions and other one-shot animations require this. */
    sealed interface Finite : AnimationSpec {
        /** Finite specs with an explicit duration; only these may be wrapped by a repeat. */
        sealed interface DurationBased : Finite
    }

    data class Tween(
        val durationMillis: Int = DefaultDurationMillis,
        val delayMillis: Int = 0,
        val easing: EasingValue = EasingValue.Preset(EasingPrimitiveToken.Standard),
    ) : Finite.DurationBased

    data class Snap(
        val delayMillis: Int = 0,
    ) : Finite.DurationBased

    data class Spring(
        val dampingRatio: Float = ComposeSpring.DampingRatioNoBouncy,
        val stiffness: Float = ComposeSpring.StiffnessMedium,
    ) : Finite

    /**
     * Repeats a [DurationBased][Finite.DurationBased] spec forever, either restarting or reversing
     * on each iteration. This is the one spec that never settles, so it is not [Finite].
     */
    data class Repeatable(
        val animation: Finite.DurationBased = Tween(),
        val repeatMode: RepeatMode = RepeatMode.Restart,
    ) : AnimationSpec

    companion object {
        const val DefaultDurationMillis: Int = 300
    }
}

/** How an [AnimationSpec.Repeatable] behaves at the end of each iteration. */
enum class RepeatMode {
    /** Jump back to the start value. */
    Restart,

    /** Play back toward the start value. */
    Reverse,
}

enum class FiniteAnimationSpecType {
    Spring,
    Tween,
    Snap,
}

fun AnimationSpec.Finite.type(): FiniteAnimationSpecType = when (this) {
    is AnimationSpec.Spring -> FiniteAnimationSpecType.Spring
    is AnimationSpec.Tween -> FiniteAnimationSpecType.Tween
    is AnimationSpec.Snap -> FiniteAnimationSpecType.Snap
}

/**
 * Resolves this finite spec to a Compose [FiniteAnimationSpec], resolving a [AnimationSpec.Tween]'s
 * easing through the given primitive map (mirroring `IndicationToken.toIndication(scheme, primitives)`).
 */
fun <T> AnimationSpec.Finite.toComposeSpec(easings: Map<EasingPrimitiveToken, Easing>): FiniteAnimationSpec<T> =
    when (this) {
        is AnimationSpec.Spring -> spring(dampingRatio = dampingRatio, stiffness = stiffness)
        is AnimationSpec.Tween -> tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing.toEasing(easings),
        )
        is AnimationSpec.Snap -> snap(delayMillis = delayMillis)
    }

@Composable
fun <T> AnimationSpec.Finite.toComposeSpec(): FiniteAnimationSpec<T> {
    return toComposeSpec(PaletteTheme.primitive.easing)
}

/** Resolves this duration-based spec to a Compose [DurationBasedAnimationSpec]. */
fun <T> AnimationSpec.Finite.DurationBased.toComposeSpec(
    easings: Map<EasingPrimitiveToken, Easing>,
): DurationBasedAnimationSpec<T> = when (this) {
    is AnimationSpec.Tween -> tween(
        durationMillis = durationMillis,
        delayMillis = delayMillis,
        easing = easing.toEasing(easings),
    )
    is AnimationSpec.Snap -> snap(delayMillis = delayMillis)
}

/** Resolves this repeating spec to a Compose [InfiniteRepeatableSpec]. */
fun <T> AnimationSpec.Repeatable.toComposeSpec(easings: Map<EasingPrimitiveToken, Easing>): InfiniteRepeatableSpec<T> =
    infiniteRepeatable(
        animation = animation.toComposeSpec(easings),
        repeatMode = when (repeatMode) {
            RepeatMode.Restart -> ComposeRepeatMode.Restart
            RepeatMode.Reverse -> ComposeRepeatMode.Reverse
        },
    )

@Composable
fun <T> AnimationSpec.Repeatable.toComposeSpec(): InfiniteRepeatableSpec<T> {
    return toComposeSpec(PaletteTheme.primitive.easing)
}
