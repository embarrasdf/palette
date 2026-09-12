package com.embarrasdf.palette.components.core

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * How a component sizes itself along an axis, independent of the container it sits in — so a
 * component (an icon, a glyph, a button) carries its own sizing intent rather than the container
 * dictating it.
 *
 * - [Fixed] — an absolute size. The mature default for standard controls; prefer a size token.
 * - [Scale] — a fraction of the available space. For elements that should track a responsive
 *   container (a media transport control that grows with its player, a chevron that tracks a row).
 * - [Fill] — all available space. For elements sized by their slot (a list-row chevron, a nav
 *   button that conforms to the bar height).
 *
 * Apply it with [size] (both axes), [height], or [width].
 */
sealed interface Sizing {
    data class Fixed(val size: Dp) : Sizing
    data class Scale(val fraction: Float) : Sizing
    data object Fill : Sizing
}

/** Applies [sizing] to both axes — a square-ish footprint. */
fun Modifier.size(sizing: Sizing): Modifier = when (sizing) {
    is Sizing.Fixed -> this.size(sizing.size)
    is Sizing.Scale -> this.fillMaxSize(sizing.fraction)
    Sizing.Fill -> this.fillMaxSize()
}

/** Applies [sizing] to the height axis only. */
fun Modifier.height(sizing: Sizing): Modifier = when (sizing) {
    is Sizing.Fixed -> this.height(sizing.size)
    is Sizing.Scale -> this.fillMaxHeight(sizing.fraction)
    Sizing.Fill -> this.fillMaxHeight()
}

/** Applies [sizing] to the width axis only. */
fun Modifier.width(sizing: Sizing): Modifier = when (sizing) {
    is Sizing.Fixed -> this.width(sizing.size)
    is Sizing.Scale -> this.fillMaxWidth(sizing.fraction)
    Sizing.Fill -> this.fillMaxWidth()
}
