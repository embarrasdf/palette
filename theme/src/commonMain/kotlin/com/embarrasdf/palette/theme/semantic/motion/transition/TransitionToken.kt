package com.embarrasdf.palette.theme.semantic.motion.transition

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.motion.Transition

enum class TransitionToken {
    Enter,
    Exit,
    PredictiveExit,
}

fun TransitionToken.toTransition(transitionScheme: TransitionScheme): Transition {
    return when (this) {
        TransitionToken.Enter -> transitionScheme.enter
        TransitionToken.Exit -> transitionScheme.exit
        TransitionToken.PredictiveExit -> transitionScheme.predictiveExit
    }
}

@Composable
fun TransitionToken.toTransition(): Transition {
    return toTransition(PaletteTheme.semantic.motion.transition)
}
