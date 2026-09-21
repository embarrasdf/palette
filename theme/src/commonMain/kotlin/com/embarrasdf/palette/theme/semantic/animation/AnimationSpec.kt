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
 * A themeable description of how a value transitions toward a target. This is Palette's
 * application-neutral counterpart to Compose's animation spec: [Spring] and [Snap] carry their
 * own parameters directly, while [Tween] references an [EasingPrimitiveToken] the way other
 * semantic tokens reference the primitive layer.
 *
 * It models one-shot transitions only (a value moving from A to B, then settling). Periodic
 * generators such as an LFO are a separate abstraction, not a variant of this type.
 */
sealed interface AnimationSpec {
    data class Spring(
        val dampingRatio: Float = ComposeSpring.DampingRatioNoBouncy,
        val stiffness: Float = ComposeSpring.StiffnessMedium,
    ) : AnimationSpec

    data class Tween(
        val durationMillis: Int = DefaultDurationMillis,
        val delayMillis: Int = 0,
        val easing: EasingPrimitiveToken = EasingPrimitiveToken.Standard,
    ) : AnimationSpec

    data class Snap(
        val delayMillis: Int = 0,
    ) : AnimationSpec

    companion object {
        const val DefaultDurationMillis: Int = 300
    }
}

enum class AnimationSpecType {
    Spring,
    Tween,
    Snap,
}

fun AnimationSpec.type(): AnimationSpecType = when (this) {
    is AnimationSpec.Spring -> AnimationSpecType.Spring
    is AnimationSpec.Tween -> AnimationSpecType.Tween
    is AnimationSpec.Snap -> AnimationSpecType.Snap
}

/**
 * Resolves this spec to a Compose [FiniteAnimationSpec], resolving a [Tween]'s easing through the
 * given primitive map (mirroring `IndicationToken.toIndication(scheme, primitives)`).
 */
fun <T> AnimationSpec.toComposeSpec(
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
fun <T> AnimationSpec.toComposeSpec(): FiniteAnimationSpec<T> {
    return toComposeSpec(PaletteTheme.primitive.easing)
}
