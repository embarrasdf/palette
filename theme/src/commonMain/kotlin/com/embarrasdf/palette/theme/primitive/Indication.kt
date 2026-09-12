package com.embarrasdf.palette.theme.primitive

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.DelegatableNode

enum class IndicationPrimitiveToken(val default: IndicationTokenSet) {
    None(IndicationTokenSet.None),
    ColorInvert(IndicationTokenSet.ColorInvert()),
    ColorSplit(IndicationTokenSet.ColorSplit()),
    Noise(IndicationTokenSet.Noise()),
    Pixelate(IndicationTokenSet.Pixelate()),
    Warp(IndicationTokenSet.Warp()),
}

data object NoOpIndication : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return object : Modifier.Node() {}
    }
}
