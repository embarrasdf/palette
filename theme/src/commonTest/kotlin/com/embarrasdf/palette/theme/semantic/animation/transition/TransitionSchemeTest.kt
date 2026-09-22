package com.embarrasdf.palette.theme.semantic.animation.transition

import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec
import kotlin.test.Test
import kotlin.test.assertEquals

class TransitionSchemeTest {

    private val scheme = TransitionScheme(
        enter = AnimationSpec.Tween(),
        exit = AnimationSpec.Snap(),
        predictiveExit = AnimationSpec.Spring(),
    )

    @Test
    fun toSpecReturnsTheSelectedToken() {
        assertEquals(scheme.enter, TransitionToken.Enter.toSpec(scheme))
        assertEquals(scheme.exit, TransitionToken.Exit.toSpec(scheme))
        assertEquals(scheme.predictiveExit, TransitionToken.PredictiveExit.toSpec(scheme))
    }

    @Test
    fun copyReplacesOnlyTheGivenToken() {
        val replacement = AnimationSpec.Snap(delayMillis = 5)
        val updated = scheme.copy(token = TransitionToken.Enter, spec = replacement)

        assertEquals(replacement, updated.enter)
        assertEquals(scheme.exit, updated.exit)
        assertEquals(scheme.predictiveExit, updated.predictiveExit)
    }
}
