package com.embarrasdf.palette.theme.primitive

import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.modifiers.ColorInvertIndication
import com.embarrasdf.palette.modifiers.ColorSplitIndication
import com.embarrasdf.palette.modifiers.ColorSplitMode
import com.embarrasdf.palette.modifiers.NoiseColorMode
import com.embarrasdf.palette.modifiers.NoiseIndication
import com.embarrasdf.palette.modifiers.PixelateIndication
import com.embarrasdf.palette.modifiers.WarpIndication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class IndicationTokenSetTest {

    @Test
    fun primitiveTokenDefaultsMatchExpectedParameters() {
        assertEquals(IndicationTokenSet.None, IndicationPrimitiveToken.None.default)
        assertEquals(
            IndicationTokenSet.ColorInvert(hoverAmount = 0.5f, pressAmount = 1f),
            IndicationPrimitiveToken.ColorInvert.default,
        )
        assertEquals(
            IndicationTokenSet.ColorSplit(
                hoverAmount = -0.02f,
                pressAmount = 0.02f,
                colorMode = ColorSplitMode.RGB,
            ),
            IndicationPrimitiveToken.ColorSplit.default,
        )
        assertEquals(
            IndicationTokenSet.Noise(
                hoverAmount = 0.25f,
                pressAmount = 1f,
                colorMode = NoiseColorMode.RandomColorFilterBlack,
            ),
            IndicationPrimitiveToken.Noise.default,
        )
        assertEquals(
            IndicationTokenSet.Pixelate(hoverSubdivisions = 2, pressSubdivisions = 6),
            IndicationPrimitiveToken.Pixelate.default,
        )
        assertEquals(
            IndicationTokenSet.Warp(pressAmount = 0.5f, pressRadius = 100.dp),
            IndicationPrimitiveToken.Warp.default,
        )
    }

    @Test
    fun primitiveTokensMapEachTokenToItsDefault() {
        val tokens = PrimitiveTokens()
        IndicationPrimitiveToken.entries.forEach { token ->
            assertEquals(token.default, tokens.indication.getValue(token))
        }
    }

    @Test
    fun toIndicationBuildsMatchingIndicationType() {
        assertEquals(NoOpIndication, IndicationTokenSet.None.toIndication())
        assertIs<ColorInvertIndication>(IndicationTokenSet.ColorInvert().toIndication())
        assertIs<ColorSplitIndication>(IndicationTokenSet.ColorSplit().toIndication())
        assertIs<NoiseIndication>(IndicationTokenSet.Noise().toIndication())
        assertIs<PixelateIndication>(IndicationTokenSet.Pixelate().toIndication())
        assertIs<WarpIndication>(IndicationTokenSet.Warp().toIndication())
    }
}
