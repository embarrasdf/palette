package com.embarrasdf.palette.theme.semantic.interaction

import androidx.compose.foundation.Indication
import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.primitive.IndicationPrimitiveToken
import com.embarrasdf.palette.theme.primitive.IndicationTokenSet

enum class IndicationToken {
    Default,
}

fun IndicationToken.primitiveToken(interactionScheme: InteractionScheme): IndicationPrimitiveToken {
    return when (this) {
        IndicationToken.Default -> interactionScheme.default
    }
}

fun IndicationToken.toIndication(
    interactionScheme: InteractionScheme,
    indicationPrimitives: Map<IndicationPrimitiveToken, IndicationTokenSet>,
): Indication {
    return indicationPrimitives.getValue(primitiveToken(interactionScheme)).toIndication()
}

@Composable
fun IndicationToken.toIndication(): Indication {
    return toIndication(PaletteTheme.semantic.interaction, PaletteTheme.primitive.indication)
}
