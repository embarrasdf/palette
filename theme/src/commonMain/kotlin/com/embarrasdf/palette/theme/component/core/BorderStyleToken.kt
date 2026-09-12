package com.embarrasdf.palette.theme.component.core

import com.embarrasdf.palette.theme.component.LocalComponentTokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.semantic.shape.ShapeToken
import com.embarrasdf.palette.components.core.BorderStyle as ComponentBorderStyle

enum class BorderStyleToken(val default: BorderStyleTokenSet) {
    Surface(
        BorderStyleTokenSet(
            width = 1.dp,
            color = ColorToken.Outline,
            shape = ShapeToken.Surface
        )
    ),
    Primary(
        BorderStyleTokenSet(
            width = 1.dp,
            color = ColorToken.Outline,
            shape = ShapeToken.Primary
        )
    ),
    Secondary(
        BorderStyleTokenSet(
            width = 1.dp,
            color = ColorToken.Outline,
            shape = ShapeToken.Secondary
        )
    ),
    Tertiary(
        BorderStyleTokenSet(
            width = Dp.Hairline,
            color = ColorToken.Outline,
            shape = ShapeToken.Tertiary
        )
    ),
}

@Composable
fun BorderStyleToken.tokenSet(): BorderStyleTokenSet = LocalComponentTokens.current.border.getValue(this)

@Composable
fun BorderStyleToken.resolve(): ComponentBorderStyle = tokenSet().toComponentStyle()
