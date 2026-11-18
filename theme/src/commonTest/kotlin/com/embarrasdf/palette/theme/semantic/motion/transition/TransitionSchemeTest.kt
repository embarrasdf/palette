package com.embarrasdf.palette.theme.semantic.motion.transition

import com.embarrasdf.palette.theme.semantic.motion.TransitionEffect
import kotlin.test.Test
import kotlin.test.assertEquals

class TransitionSchemeTest {

    private val scheme = TransitionScheme(
        enter = listOf(TransitionEffect.Fade()),
        exit = listOf(TransitionEffect.Translate()),
        predictiveExit = listOf(TransitionEffect.Scale()),
    )

    @Test
    fun toTransitionReturnsTheSelectedToken() {
        assertEquals(scheme.enter, TransitionToken.Enter.toTransition(scheme))
        assertEquals(scheme.exit, TransitionToken.Exit.toTransition(scheme))
        assertEquals(scheme.predictiveExit, TransitionToken.PredictiveExit.toTransition(scheme))
    }

    @Test
    fun copyReplacesOnlyTheGivenToken() {
        val replacement = listOf(TransitionEffect.Fade(), TransitionEffect.Scale())
        val updated = scheme.copy(token = TransitionToken.Enter, transition = replacement)

        assertEquals(replacement, updated.enter)
        assertEquals(scheme.exit, updated.exit)
        assertEquals(scheme.predictiveExit, updated.predictiveExit)
    }
}
