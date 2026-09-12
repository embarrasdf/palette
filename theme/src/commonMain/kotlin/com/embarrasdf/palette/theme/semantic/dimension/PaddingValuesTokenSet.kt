package com.embarrasdf.palette.theme.semantic.dimension

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.spacing.Spacing
import com.embarrasdf.palette.theme.semantic.spacing.SpacingToken
import com.embarrasdf.palette.theme.semantic.spacing.toSpacing

data class PaddingValuesTokenSet(
    val start: SpacingToken,
    val top: SpacingToken,
    val end: SpacingToken,
    val bottom: SpacingToken,
)

fun PaddingValuesTokenSet(all: SpacingToken) = PaddingValuesTokenSet(
    start = all,
    top = all,
    end = all,
    bottom = all,
)

fun PaddingValuesTokenSet.toPaddingValues(spacing: Spacing): PaddingValues = PaddingValues(
    start = start.toSpacing(spacing),
    top = top.toSpacing(spacing),
    end = end.toSpacing(spacing),
    bottom = bottom.toSpacing(spacing),
)

@Composable
fun PaddingValuesTokenSet.toPaddingValues(): PaddingValues =
    toPaddingValues(PaletteTheme.semantic.dimension.spacing)
