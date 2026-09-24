package com.embarrasdf.palette.theme.semantic.motion

import kotlin.test.Test
import kotlin.test.assertEquals

class MotionSchemeTest {

    private val scheme = MotionScheme(
        enter = Transition.SharedAxis(),
        exit = Transition.Fade(),
        predictiveExit = Transition.Scale(),
    )

    @Test
    fun toTransitionReturnsTheSelectedToken() {
        assertEquals(scheme.enter, MotionToken.Enter.toTransition(scheme))
        assertEquals(scheme.exit, MotionToken.Exit.toTransition(scheme))
        assertEquals(scheme.predictiveExit, MotionToken.PredictiveExit.toTransition(scheme))
    }

    @Test
    fun copyReplacesOnlyTheGivenToken() {
        val replacement = Transition.None
        val updated = scheme.copy(token = MotionToken.Enter, transition = replacement)

        assertEquals(replacement, updated.enter)
        assertEquals(scheme.exit, updated.exit)
        assertEquals(scheme.predictiveExit, updated.predictiveExit)
    }

    @Test
    fun typeMatchesTheVariant() {
        assertEquals(TransitionType.SharedAxis, Transition.SharedAxis().type())
        assertEquals(TransitionType.Fade, Transition.Fade().type())
        assertEquals(TransitionType.Scale, Transition.Scale().type())
        assertEquals(TransitionType.None, Transition.None.type())
    }
}
