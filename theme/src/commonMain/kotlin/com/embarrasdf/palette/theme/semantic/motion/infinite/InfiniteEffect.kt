package com.embarrasdf.palette.theme.semantic.motion.infinite

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.primitive.EasingPrimitiveToken
import com.embarrasdf.palette.theme.semantic.animation.AnimationScheme
import com.embarrasdf.palette.theme.semantic.animation.infinite.InfiniteAnimationToken
import com.embarrasdf.palette.theme.semantic.animation.infinite.toSpec
import com.embarrasdf.palette.theme.semantic.animation.toComposeSpec

/**
 * A continuous, looping motion applied to a persistent element. Each effect references the
 * value-animation tempo that drives it through an [InfiniteAnimationToken] and resolves to a
 * [Modifier].
 */
sealed interface InfiniteEffect {
    val animation: InfiniteAnimationToken

    /** Rotates the element continuously through a full turn. */
    data class Spin(
        override val animation: InfiniteAnimationToken = InfiniteAnimationToken.Default,
    ) : InfiniteEffect
}

enum class InfiniteEffectType {
    Spin,
}

fun InfiniteEffect.type(): InfiniteEffectType = when (this) {
    is InfiniteEffect.Spin -> InfiniteEffectType.Spin
}

@Composable
fun InfiniteEffect.toModifier(
    animationScheme: AnimationScheme,
    easings: Map<EasingPrimitiveToken, Easing>,
): Modifier {
    val spec = animation.toSpec(animationScheme).toComposeSpec<Float>(easings)
    return when (this) {
        is InfiniteEffect.Spin -> {
            val transition = rememberInfiniteTransition(label = "Spin")
            val angle by transition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = spec,
                label = "Spin.angle",
            )
            Modifier.graphicsLayer { rotationZ = angle }
        }
    }
}

@Composable
fun InfiniteEffect.toModifier(): Modifier = toModifier(
    animationScheme = PaletteTheme.semantic.animation,
    easings = PaletteTheme.primitive.easing,
)
