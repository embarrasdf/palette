package com.embarrasdf.palette.theme.semantic.animation

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatableSpec
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.primitive.EasingCurve
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
 * Looping is a combinator, not a mechanism, and mirrors Compose's own split: [Repeatable] repeats a
 * [DurationBased] spec a fixed number of times and then settles (so it is [Finite], matching
 * `RepeatableSpec`), while [InfiniteRepeatable] repeats forever and so is the one spec that never
 * settles (matching `InfiniteRepeatableSpec`). Velocity-based, target-less specs (fling / friction)
 * are intentionally *not* part of this hierarchy; see [DecaySpec].
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
     * Repeats a [DurationBased][Finite.DurationBased] spec a fixed number of [iterations], restarting
     * or reversing after each one, then settles on the final value. Because it terminates it is
     * [Finite] and may be used anywhere a one-shot spec is required.
     */
    data class Repeatable(
        val iterations: Int = DefaultIterations,
        val animation: Finite.DurationBased = Tween(),
        val repeatMode: RepeatMode = RepeatMode.Restart,
    ) : Finite

    /**
     * Repeats a [DurationBased][Finite.DurationBased] spec forever, either restarting or reversing
     * on each iteration. This is the one spec that never settles, so it is not [Finite].
     */
    data class InfiniteRepeatable(
        val animation: Finite.DurationBased = Tween(),
        val repeatMode: RepeatMode = RepeatMode.Restart,
    ) : AnimationSpec

    companion object {
        const val DefaultDurationMillis: Int = 300
        const val DefaultIterations: Int = 2
    }
}

/** How a repeating spec ([AnimationSpec.Repeatable] / [AnimationSpec.InfiniteRepeatable]) behaves at the end of each iteration. */
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
    Repeatable,
}

fun AnimationSpec.Finite.type(): FiniteAnimationSpecType = when (this) {
    is AnimationSpec.Spring -> FiniteAnimationSpecType.Spring
    is AnimationSpec.Tween -> FiniteAnimationSpecType.Tween
    is AnimationSpec.Snap -> FiniteAnimationSpecType.Snap
    is AnimationSpec.Repeatable -> FiniteAnimationSpecType.Repeatable
}

/**
 * Resolves this finite spec to a Compose [FiniteAnimationSpec], resolving a [AnimationSpec.Tween]'s
 * easing through the given primitive map (mirroring `IndicationToken.toIndication(scheme, primitives)`).
 */
fun <T> AnimationSpec.Finite.toComposeSpec(easings: Map<EasingPrimitiveToken, EasingCurve>): FiniteAnimationSpec<T> =
    when (this) {
        is AnimationSpec.Spring -> spring(dampingRatio = dampingRatio, stiffness = stiffness)
        is AnimationSpec.Tween -> tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing.toEasing(easings),
        )
        is AnimationSpec.Snap -> snap(delayMillis = delayMillis)
        is AnimationSpec.Repeatable -> toComposeSpec(easings)
    }

/** Resolves this finite repeating spec to a Compose [RepeatableSpec]. */
fun <T> AnimationSpec.Repeatable.toComposeSpec(easings: Map<EasingPrimitiveToken, EasingCurve>): RepeatableSpec<T> =
    repeatable(
        iterations = iterations,
        animation = animation.toComposeSpec(easings),
        repeatMode = when (repeatMode) {
            RepeatMode.Restart -> ComposeRepeatMode.Restart
            RepeatMode.Reverse -> ComposeRepeatMode.Reverse
        },
    )

@Composable
fun <T> AnimationSpec.Repeatable.toComposeSpec(): RepeatableSpec<T> {
    return toComposeSpec(PaletteTheme.primitive.easing)
}

@Composable
fun <T> AnimationSpec.Finite.toComposeSpec(): FiniteAnimationSpec<T> {
    return toComposeSpec(PaletteTheme.primitive.easing)
}

/** Resolves this duration-based spec to a Compose [DurationBasedAnimationSpec]. */
fun <T> AnimationSpec.Finite.DurationBased.toComposeSpec(
    easings: Map<EasingPrimitiveToken, EasingCurve>,
): DurationBasedAnimationSpec<T> = when (this) {
    is AnimationSpec.Tween -> tween(
        durationMillis = durationMillis,
        delayMillis = delayMillis,
        easing = easing.toEasing(easings),
    )
    is AnimationSpec.Snap -> snap(delayMillis = delayMillis)
}

/** Resolves this forever-repeating spec to a Compose [InfiniteRepeatableSpec]. */
fun <T> AnimationSpec.InfiniteRepeatable.toComposeSpec(
    easings: Map<EasingPrimitiveToken, EasingCurve>,
): InfiniteRepeatableSpec<T> = infiniteRepeatable(
    animation = animation.toComposeSpec(easings),
    repeatMode = when (repeatMode) {
        RepeatMode.Restart -> ComposeRepeatMode.Restart
        RepeatMode.Reverse -> ComposeRepeatMode.Reverse
    },
)

@Composable
fun <T> AnimationSpec.InfiniteRepeatable.toComposeSpec(): InfiniteRepeatableSpec<T> {
    return toComposeSpec(PaletteTheme.primitive.easing)
}
