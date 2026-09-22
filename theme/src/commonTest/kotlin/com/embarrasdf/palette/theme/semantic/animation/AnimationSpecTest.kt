package com.embarrasdf.palette.theme.semantic.animation

import androidx.compose.animation.core.SnapSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import com.embarrasdf.palette.theme.primitive.EasingPrimitiveToken
import com.embarrasdf.palette.theme.primitive.PrimitiveTokens
import kotlin.test.Test
import kotlin.test.assertEquals

class AnimationSpecTest {

    private val easings = PrimitiveTokens().easing

    @Test
    fun typeMatchesTheVariant() {
        assertEquals(AnimationSpecType.Spring, AnimationSpec.Spring().type())
        assertEquals(AnimationSpecType.Tween, AnimationSpec.Tween().type())
        assertEquals(AnimationSpecType.Snap, AnimationSpec.Snap().type())
    }

    @Test
    fun tweenResolvesEasingThroughThePrimitiveMap() {
        val spec = AnimationSpec.Tween(
            durationMillis = 500,
            delayMillis = 50,
            easing = EasingPrimitiveToken.Emphasized,
        ).toComposeSpec<Float>(easings) as TweenSpec

        assertEquals(500, spec.durationMillis)
        assertEquals(50, spec.delay)
        assertEquals(easings.getValue(EasingPrimitiveToken.Emphasized), spec.easing)
    }

    @Test
    fun springResolvesToSpringSpec() {
        val spec = AnimationSpec.Spring(
            dampingRatio = 0.5f,
            stiffness = 800f,
        ).toComposeSpec<Float>(easings) as SpringSpec

        assertEquals(0.5f, spec.dampingRatio)
        assertEquals(800f, spec.stiffness)
    }

    @Test
    fun snapResolvesToSnapSpec() {
        val spec = AnimationSpec.Snap(delayMillis = 120)
            .toComposeSpec<Float>(easings) as SnapSpec

        assertEquals(120, spec.delay)
    }
}
