package com.embarrasdf.palette.theme.semantic.shape

import com.embarrasdf.palette.theme.primitive.PrimitiveTokens
import com.embarrasdf.palette.theme.primitive.ShapePrimitiveToken
import kotlin.test.Test
import kotlin.test.assertEquals

class ShapeTokenTest {

    private val shapePrimitives = PrimitiveTokens().shape

    private val scheme = ShapeScheme(
        primary = ShapePrimitiveToken.Circle,
        secondary = ShapePrimitiveToken.RoundRect,
        tertiary = ShapePrimitiveToken.Triangle,
        surface = ShapePrimitiveToken.Diamond,
    )

    @Test
    fun primitiveTokenReturnsTheSchemeSelection() {
        assertEquals(ShapePrimitiveToken.Circle, ShapeToken.Primary.primitiveToken(scheme))
        assertEquals(ShapePrimitiveToken.RoundRect, ShapeToken.Secondary.primitiveToken(scheme))
        assertEquals(ShapePrimitiveToken.Triangle, ShapeToken.Tertiary.primitiveToken(scheme))
        assertEquals(ShapePrimitiveToken.Diamond, ShapeToken.Surface.primitiveToken(scheme))
    }

    @Test
    fun toShapeResolvesSelectionThroughPrimitiveMap() {
        assertEquals(
            shapePrimitives.getValue(ShapePrimitiveToken.Circle),
            ShapeToken.Primary.toShape(scheme, shapePrimitives),
        )
        assertEquals(
            shapePrimitives.getValue(ShapePrimitiveToken.Diamond),
            ShapeToken.Surface.toShape(scheme, shapePrimitives),
        )
    }

    @Test
    fun copyReplacesOnlyTheGivenToken() {
        val updated = scheme.copy(
            token = ShapeToken.Primary,
            shapePrimitiveToken = ShapePrimitiveToken.Rectangle,
        )
        assertEquals(ShapePrimitiveToken.Rectangle, updated.primary)
        assertEquals(scheme.secondary, updated.secondary)
        assertEquals(scheme.tertiary, updated.tertiary)
        assertEquals(scheme.surface, updated.surface)
    }
}
