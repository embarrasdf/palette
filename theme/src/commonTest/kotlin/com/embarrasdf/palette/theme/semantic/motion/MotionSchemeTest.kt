package com.embarrasdf.palette.theme.semantic.motion

import kotlin.test.Test
import kotlin.test.assertEquals

class MotionSchemeTest {

    private val scheme = MotionScheme(
        enter = listOf(TransitionEffect.Fade()),
        exit = listOf(TransitionEffect.Translate()),
        predictiveExit = listOf(TransitionEffect.Scale()),
    )

    @Test
    fun toTransitionReturnsTheSelectedToken() {
        assertEquals(scheme.enter, MotionToken.Enter.toTransition(scheme))
        assertEquals(scheme.exit, MotionToken.Exit.toTransition(scheme))
        assertEquals(scheme.predictiveExit, MotionToken.PredictiveExit.toTransition(scheme))
    }

    @Test
    fun copyReplacesOnlyTheGivenToken() {
        val replacement = listOf(TransitionEffect.Fade(), TransitionEffect.Scale())
        val updated = scheme.copy(token = MotionToken.Enter, transition = replacement)

        assertEquals(replacement, updated.enter)
        assertEquals(scheme.exit, updated.exit)
        assertEquals(scheme.predictiveExit, updated.predictiveExit)
    }

    @Test
    fun typeMatchesTheVariant() {
        assertEquals(TransitionEffectType.Fade, TransitionEffect.Fade().type())
        assertEquals(TransitionEffectType.Scale, TransitionEffect.Scale().type())
        assertEquals(TransitionEffectType.Translate, TransitionEffect.Translate().type())
    }
}
