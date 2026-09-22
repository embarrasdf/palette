package com.embarrasdf.palette.theme.semantic.animation.transition

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.animation.AnimationSpec

enum class TransitionToken {
    Enter,
    Exit,
    PredictiveExit,
}

fun TransitionToken.toSpec(scheme: TransitionScheme): AnimationSpec.Finite {
    return when (this) {
        TransitionToken.Enter -> scheme.enter
        TransitionToken.Exit -> scheme.exit
        TransitionToken.PredictiveExit -> scheme.predictiveExit
    }
}

@Composable
fun TransitionToken.toSpec(): AnimationSpec.Finite {
    return toSpec(PaletteTheme.semantic.animation.transition)
}
