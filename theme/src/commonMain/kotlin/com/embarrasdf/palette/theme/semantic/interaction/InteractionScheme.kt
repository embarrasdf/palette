package com.embarrasdf.palette.theme.semantic.interaction

import com.embarrasdf.palette.theme.primitive.IndicationPrimitiveToken

data class InteractionScheme(
    val default: IndicationPrimitiveToken,
)

val PaletteInteractionScheme = InteractionScheme(
    default = IndicationPrimitiveToken.ColorSplit,
)

fun InteractionScheme.copy(
    token: IndicationToken,
    indicationPrimitiveToken: IndicationPrimitiveToken,
) = this.copy(
    default = if (token == IndicationToken.Default) indicationPrimitiveToken else this.default,
)
