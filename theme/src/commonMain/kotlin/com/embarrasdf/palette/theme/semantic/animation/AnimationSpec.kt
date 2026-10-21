package com.embarrasdf.palette.theme.semantic.animation

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.InfiniteRepeatableSpec
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
 * Two mechanism families generate motion:
 *
 * - [DurationBased] — a curve sampled over an explicit duration ([Tween], [Snap]). Only these may be
 *   wrapped by a [Repeat], since repeating wraps time around a known cycle length.
 * - [PhysicsBased] — a simulation that settles on thresholds ([Spring]), with no fixed duration.
 *
 * [Finite] crosscuts them as the "settles" marker that one-shot consumers (transitions) require;
 * both mechanism families are [Finite]. [Repeat] is a combinator that loops a [DurationBased] spec:
 * [Repeat.Finite] repeats a fixed number of times and then settles (so it too is [Finite]), while
 * [Repeat.Infinite] loops forever and so is the one spec that never settles. Velocity-based,
 * target-less specs (fling / friction) are intentionally *not* part of this hierarchy — they would
 * be a separate decay spec, which is not modeled yet.
 */
sealed interface AnimationSpec {

    /** Specs that settle on a final value. Transitions and other one-shot animations require this. */
    sealed interface Finite : AnimationSpec

    /** A curve sampled over an explicit duration; only these may be wrapped by a [Repeat]. */
    sealed interface DurationBased : Finite

    /** A physics simulation that settles on thresholds, with no fixed duration. */
    sealed interface PhysicsBased : Finite

    data class Tween(
        val durationMillis: Int = DefaultDurationMillis,
        val delayMillis: Int = 0,
        val easing: EasingValue = EasingValue.Preset(EasingPrimitiveToken.Standard),
    ) : DurationBased

    data class Snap(
        val delayMillis: Int = 0,
    ) : DurationBased

    data class Spring(
        val dampingRatio: Float = ComposeSpring.DampingRatioNoBouncy,
        val stiffness: Float = ComposeSpring.StiffnessMedium,
    ) : PhysicsBased

    /**
     * Loops a [DurationBased] mechanism, restarting or reversing after each iteration. The variant
     * records whether the loop terminates: [Finite] settles after a set number of iterations,
     * [Infinite] never does.
     */
    sealed interface Repeat : AnimationSpec {
        val animation: DurationBased
        val repeatMode: RepeatMode

        /**
         * Repeats [animation] a fixed number of [iterations], then settles on the final value.
         * Because it terminates it is [AnimationSpec.Finite] and may be used anywhere a one-shot
         * spec is required.
         */
        data class Finite(
            override val animation: DurationBased = Tween(),
            val iterations: Int = DefaultIterations,
            override val repeatMode: RepeatMode = RepeatMode.Restart,
        ) : Repeat, AnimationSpec.Finite

        /** Repeats [animation] forever; the one spec that never settles. */
        data class Infinite(
            override val animation: DurationBased = Tween(),
            override val repeatMode: RepeatMode = RepeatMode.Restart,
        ) : Repeat
    }

    companion object {
        const val DefaultDurationMillis: Int = 300
        const val DefaultIterations: Int = 2
    }
}

/** How a [AnimationSpec.Repeat] behaves at the end of each iteration. */
enum class RepeatMode {
    /** Jump back to the start value. */
    Restart,

    /** Play back toward the start value. */
    Reverse,
}

private fun RepeatMode.toCompose(): ComposeRepeatMode = when (this) {
    RepeatMode.Restart -> ComposeRepeatMode.Restart
    RepeatMode.Reverse -> ComposeRepeatMode.Reverse
}

/** Time one pass of this duration-based spec occupies, in milliseconds. */
private val AnimationSpec.DurationBased.totalDurationMillis: Int
    get() = when (this) {
        is AnimationSpec.Tween -> durationMillis + delayMillis
        is AnimationSpec.Snap -> delayMillis
    }

/**
 * The driver to repeat, guaranteed to occupy a positive cycle. Compose's repeatable specs divide by
 * the driver's duration, so a zero-duration driver — a default [AnimationSpec.Snap] (no delay) or a
 * [AnimationSpec.Tween] with `durationMillis` 0 — would divide by zero. A driver that already spans a
 * positive time (including a [AnimationSpec.Snap] with a delay, a real periodic strobe) is kept as-is.
 */
private fun AnimationSpec.DurationBased.repeatDriver(): AnimationSpec.DurationBased =
    if (totalDurationMillis > 0) this else AnimationSpec.Tween(durationMillis = 1)

/** The leaf mechanisms a finite spec presents as in a demo type selector. */
enum class FiniteAnimationSpecType {
    Spring,
    Tween,
    Snap,
}

/**
 * The leaf mechanism this finite spec presents as. A [AnimationSpec.Repeat.Finite] classifies as the
 * [DurationBased] mechanism it repeats.
 */
fun AnimationSpec.Finite.type(): FiniteAnimationSpecType = when (this) {
    is AnimationSpec.Spring -> FiniteAnimationSpecType.Spring
    is AnimationSpec.Tween -> FiniteAnimationSpecType.Tween
    is AnimationSpec.Snap -> FiniteAnimationSpecType.Snap
    is AnimationSpec.Repeat.Finite -> animation.type()
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
        is AnimationSpec.Repeat.Finite -> repeatable(
            iterations = iterations,
            animation = animation.repeatDriver().toComposeSpec(easings),
            repeatMode = repeatMode.toCompose(),
        )
    }

@Composable
fun <T> AnimationSpec.Finite.toComposeSpec(): FiniteAnimationSpec<T> {
    return toComposeSpec(PaletteTheme.primitive.easing)
}

/** Resolves this duration-based spec to a Compose [DurationBasedAnimationSpec]. */
fun <T> AnimationSpec.DurationBased.toComposeSpec(
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
fun <T> AnimationSpec.Repeat.Infinite.toComposeSpec(
    easings: Map<EasingPrimitiveToken, EasingCurve>,
): InfiniteRepeatableSpec<T> = infiniteRepeatable(
    animation = animation.repeatDriver().toComposeSpec(easings),
    repeatMode = repeatMode.toCompose(),
)

@Composable
fun <T> AnimationSpec.Repeat.Infinite.toComposeSpec(): InfiniteRepeatableSpec<T> {
    return toComposeSpec(PaletteTheme.primitive.easing)
}
