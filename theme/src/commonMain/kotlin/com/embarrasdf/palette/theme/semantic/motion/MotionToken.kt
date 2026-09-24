package com.embarrasdf.palette.theme.semantic.motion

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme

enum class MotionToken {
    Enter,
    Exit,
    PredictiveExit,
}

fun MotionToken.toTransition(motionScheme: MotionScheme): Transition {
    return when (this) {
        MotionToken.Enter -> motionScheme.enter
        MotionToken.Exit -> motionScheme.exit
        MotionToken.PredictiveExit -> motionScheme.predictiveExit
    }
}

@Composable
fun MotionToken.toTransition(): Transition {
    return toTransition(PaletteTheme.semantic.motion)
}
