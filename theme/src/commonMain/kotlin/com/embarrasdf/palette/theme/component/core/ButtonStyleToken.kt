package com.embarrasdf.palette.theme.component.core

import com.embarrasdf.palette.theme.component.LocalComponentTokens

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.semantic.shape.ShapeToken

enum class ButtonStyleToken(val default: ButtonStyleTokenSet) {
    Primary(
        ButtonStyleTokenSet(
            containerColor = ColorToken.Primary,
            shape = ShapeToken.Primary,
            borderStyle = BorderStyleToken.Primary,
        ),
    ),
    Secondary(
        ButtonStyleTokenSet(
            containerColor = ColorToken.Surface,
            shape = ShapeToken.Secondary,
            borderStyle = BorderStyleToken.Secondary,
        ),
    ),
    Tertiary(
        ButtonStyleTokenSet(
            containerColor = ColorToken.OnPrimary,
            shape = ShapeToken.Tertiary,
            borderStyle = BorderStyleToken.Tertiary,
        ),
    ),
}

@Composable
fun ButtonStyleToken.tokenSet(): ButtonStyleTokenSet =
    LocalComponentTokens.current.button.getValue(this)

@Composable
fun ButtonStyleToken.resolve(): ButtonStyle = tokenSet().toComponentStyle()
