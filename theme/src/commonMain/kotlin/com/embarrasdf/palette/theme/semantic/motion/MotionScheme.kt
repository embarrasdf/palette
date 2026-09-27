package com.embarrasdf.palette.theme.semantic.motion

import com.embarrasdf.palette.theme.semantic.motion.infinite.InfiniteMotionScheme
import com.embarrasdf.palette.theme.semantic.motion.infinite.PaletteInfiniteMotionScheme
import com.embarrasdf.palette.theme.semantic.motion.transition.PaletteTransitionScheme
import com.embarrasdf.palette.theme.semantic.motion.transition.TransitionScheme

/**
 * Motion choreography, grouped by family: [transition] holds one-shot navigation transitions,
 * [infinite] holds continuous looping effects.
 */
data class MotionScheme(
    val transition: TransitionScheme = PaletteTransitionScheme,
    val infinite: InfiniteMotionScheme = PaletteInfiniteMotionScheme,
)

val PaletteMotionScheme = MotionScheme()
