package com.embarrasdf.palette.theme.semantic.interaction

import com.embarrasdf.palette.modifiers.NoiseIndication
import com.embarrasdf.palette.modifiers.WarpIndication
import com.embarrasdf.palette.theme.primitive.IndicationPrimitiveToken
import com.embarrasdf.palette.theme.primitive.PrimitiveTokens
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class IndicationTokenTest {

    private val indicationPrimitives = PrimitiveTokens().indication

    @Test
    fun primitiveTokenReturnsTheSchemeSelection() {
        val scheme = InteractionScheme(default = IndicationPrimitiveToken.Pixelate)
        assertEquals(IndicationPrimitiveToken.Pixelate, IndicationToken.Default.primitiveToken(scheme))
    }

    @Test
    fun toIndicationResolvesSelectionThroughPrimitiveMap() {
        val scheme = InteractionScheme(default = IndicationPrimitiveToken.Noise)
        assertIs<NoiseIndication>(IndicationToken.Default.toIndication(scheme, indicationPrimitives))
    }

    @Test
    fun copyReplacesTheDefaultToken() {
        val updated = PaletteInteractionScheme.copy(
            token = IndicationToken.Default,
            indicationPrimitiveToken = IndicationPrimitiveToken.Warp,
        )
        assertEquals(IndicationPrimitiveToken.Warp, updated.default)
        assertIs<WarpIndication>(IndicationToken.Default.toIndication(updated, indicationPrimitives))
    }
}
