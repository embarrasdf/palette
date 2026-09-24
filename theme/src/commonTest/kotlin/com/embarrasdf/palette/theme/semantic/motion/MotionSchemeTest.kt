package com.embarrasdf.palette.theme.semantic.motion

import com.embarrasdf.palette.theme.semantic.animation.AnimationToken
import kotlin.test.Test
import kotlin.test.assertEquals

class MotionSchemeTest {

    private val scheme = MotionScheme(
        enter = Transition(MotionPattern.SharedAxis(), AnimationToken.Default),
        exit = Transition(MotionPattern.Fade, AnimationToken.Default),
        predictiveExit = Transition(MotionPattern.Scale(), AnimationToken.Default),
    )

    @Test
    fun toTransitionReturnsTheSelectedToken() {
        assertEquals(scheme.enter, MotionToken.Enter.toTransition(scheme))
        assertEquals(scheme.exit, MotionToken.Exit.toTransition(scheme))
        assertEquals(scheme.predictiveExit, MotionToken.PredictiveExit.toTransition(scheme))
    }

    @Test
    fun copyReplacesOnlyTheGivenToken() {
        val replacement = Transition(MotionPattern.Fade)
        val updated = scheme.copy(token = MotionToken.Enter, transition = replacement)

        assertEquals(replacement, updated.enter)
        assertEquals(scheme.exit, updated.exit)
        assertEquals(scheme.predictiveExit, updated.predictiveExit)
    }

    @Test
    fun patternTypeMatchesTheVariant() {
        assertEquals(MotionPatternType.SharedAxis, MotionPattern.SharedAxis().type())
        assertEquals(MotionPatternType.Fade, MotionPattern.Fade.type())
        assertEquals(MotionPatternType.Scale, MotionPattern.Scale().type())
    }
}
