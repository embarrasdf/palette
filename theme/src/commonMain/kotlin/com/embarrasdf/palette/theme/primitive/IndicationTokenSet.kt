package com.embarrasdf.palette.theme.primitive

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.modifiers.ColorInvertIndication
import com.embarrasdf.palette.modifiers.ColorSplitIndication
import com.embarrasdf.palette.modifiers.ColorSplitMode
import com.embarrasdf.palette.modifiers.NoiseColorMode
import com.embarrasdf.palette.modifiers.NoiseIndication
import com.embarrasdf.palette.modifiers.PixelateIndication
import com.embarrasdf.palette.modifiers.WarpIndication

sealed interface IndicationTokenSet {
    fun toIndication(): Indication

    data object None : IndicationTokenSet {
        override fun toIndication(): Indication = NoOpIndication
    }

    data class ColorInvert(
        val hoverAmount: Float = 0.5f,
        val pressAmount: Float = 1f,
    ) : IndicationTokenSet {
        override fun toIndication(): Indication = ColorInvertIndication(
            amount = { interaction ->
                when (interaction) {
                    is HoverInteraction.Enter -> hoverAmount
                    is PressInteraction.Press -> pressAmount
                    else -> 0f
                }
            },
        )
    }

    data class ColorSplit(
        val hoverAmount: Float = -0.02f,
        val pressAmount: Float = 0.02f,
        val colorMode: ColorSplitMode = ColorSplitMode.RGB,
    ) : IndicationTokenSet {
        override fun toIndication(): Indication = ColorSplitIndication(
            xAmount = { interaction -> amountFor(interaction) },
            yAmount = { interaction -> amountFor(interaction) },
            colorMode = colorMode,
        )

        private fun amountFor(interaction: Interaction): Float =
            when (interaction) {
                is HoverInteraction.Enter -> hoverAmount
                is PressInteraction.Press -> pressAmount
                else -> 0f
            }
    }

    data class Noise(
        val hoverAmount: Float = 0.25f,
        val pressAmount: Float = 1f,
        val colorMode: NoiseColorMode = NoiseColorMode.RandomColorFilterBlack,
    ) : IndicationTokenSet {
        override fun toIndication(): Indication = NoiseIndication(
            colorMode = colorMode,
            amount = { interaction ->
                when (interaction) {
                    is HoverInteraction.Enter -> hoverAmount
                    is PressInteraction.Press -> pressAmount
                    else -> 0f
                }
            },
        )
    }

    data class Pixelate(
        val hoverSubdivisions: Int = 2,
        val pressSubdivisions: Int = 6,
    ) : IndicationTokenSet {
        override fun toIndication(): Indication = PixelateIndication(
            subdivisions = { interaction ->
                when (interaction) {
                    is HoverInteraction.Enter -> hoverSubdivisions
                    is PressInteraction.Press -> pressSubdivisions
                    else -> 0
                }
            },
        )
    }

    data class Warp(
        val pressAmount: Float = 0.5f,
        val pressRadius: Dp = 100.dp,
    ) : IndicationTokenSet {
        override fun toIndication(): Indication = WarpIndication(
            point = { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> interaction.pressPosition
                    is PressInteraction.Release -> interaction.press.pressPosition
                    is PressInteraction.Cancel -> interaction.press.pressPosition
                    else -> Offset.Zero
                }
            },
            radius = { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> pressRadius
                    else -> 0.dp
                }
            },
            amount = { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> pressAmount
                    else -> 0f
                }
            },
        )
    }
}
