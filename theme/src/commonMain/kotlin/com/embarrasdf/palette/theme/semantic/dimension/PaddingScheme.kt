package com.embarrasdf.palette.theme.semantic.dimension

import com.embarrasdf.palette.theme.semantic.spacing.SpacingToken

data class PaddingScheme(
    val wide: PaddingValuesTokenSet,
)

val PalettePaddingScheme = PaddingScheme(
    wide = PaddingValuesTokenSet(
        start = SpacingToken.Large,
        top = SpacingToken.Small,
        end = SpacingToken.Large,
        bottom = SpacingToken.Small,
    ),
)

fun PaddingScheme.copy(
    token: PaddingValuesToken,
    tokenSet: PaddingValuesTokenSet,
) = this.copy(
    wide = if (token == PaddingValuesToken.Wide) tokenSet else this.wide,
)

fun PaddingScheme.tokenSet(token: PaddingValuesToken): PaddingValuesTokenSet = when (token) {
    PaddingValuesToken.Wide -> wide
}
