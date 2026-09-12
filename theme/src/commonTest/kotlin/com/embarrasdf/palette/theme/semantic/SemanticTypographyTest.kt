package com.embarrasdf.palette.theme.semantic

import androidx.compose.ui.unit.sp
import com.embarrasdf.palette.theme.primitive.FontFamily
import com.embarrasdf.palette.theme.primitive.PrimitiveTokens
import com.embarrasdf.palette.theme.semantic.typography.SemanticTypography
import com.embarrasdf.palette.theme.semantic.typography.TypographyToken
import com.embarrasdf.palette.theme.semantic.typography.resolve
import com.embarrasdf.palette.theme.semantic.typography.toComposeTextStyle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SemanticTypographyTest {

    private val primitiveTokens = PrimitiveTokens()

    @Test
    fun defaultResolvesEachTokenFromItsDefaultSet() {
        val resolved = SemanticTypography().resolve(primitiveTokens)

        assertEquals(
            TypographyToken.Display.default.toComposeTextStyle(primitiveTokens),
            resolved.display,
        )
        assertEquals(
            TypographyToken.LabelSmall.default.toComposeTextStyle(primitiveTokens),
            resolved.labelSmall,
        )
    }

    @Test
    fun withTokenSetPinsTokenAndLeavesOthersAtDefault() {
        val override = TypographyToken.TitleLarge.default.copy(fontSize = 99.sp)
        val resolved = SemanticTypography()
            .withTokenSet(TypographyToken.TitleLarge, override)
            .resolve(primitiveTokens)

        assertEquals(99.sp, resolved.titleLarge.fontSize)
        assertEquals(
            TypographyToken.Display.default.toComposeTextStyle(primitiveTokens),
            resolved.display,
        )
    }

    @Test
    fun fontFamilySelectionResolvesThroughPrimitiveTokens() {
        val monospace = SemanticTypography().resolve(primitiveTokens)
        val serif = SemanticTypography()
            .withTokenSet(
                TypographyToken.Display,
                TypographyToken.Display.default.copy(fontFamily = FontFamily.Serif),
            )
            .resolve(primitiveTokens)

        assertEquals(
            primitiveTokens.fontFamily.getValue(FontFamily.Serif),
            serif.display.fontFamily,
        )
        assertNotEquals(monospace.display.fontFamily, serif.display.fontFamily)
    }

    @Test
    fun withTokenSetAccumulatesSelections() {
        val custom = TypographyToken.TitleLarge.default.copy(fontFamily = FontFamily.Serif)
        val semantic = SemanticTypography()
            .withTokenSet(TypographyToken.TitleLarge, custom)
            .withTokenSet(
                TypographyToken.Display,
                TypographyToken.Display.default.copy(fontFamily = FontFamily.SansSerif),
            )

        assertEquals(custom, semantic.tokens.getValue(TypographyToken.TitleLarge))
        assertEquals(FontFamily.SansSerif, semantic.tokens.getValue(TypographyToken.Display).fontFamily)
    }
}
