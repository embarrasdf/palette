package com.embarrasdf.palette.theme.semantic.dimension

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.spacing.Spacing

enum class PaddingValuesToken {
    Wide,
}

fun PaddingValuesToken.tokenSet(scheme: PaddingScheme): PaddingValuesTokenSet {
    return when (this) {
        PaddingValuesToken.Wide -> scheme.wide
    }
}

fun PaddingValuesToken.toPaddingValues(
    scheme: PaddingScheme,
    spacing: Spacing,
): PaddingValues = tokenSet(scheme).toPaddingValues(spacing)

@Composable
fun PaddingValuesToken.toPaddingValues(): PaddingValues = toPaddingValues(
    scheme = PaletteTheme.semantic.dimension.padding,
    spacing = PaletteTheme.semantic.dimension.spacing,
)
