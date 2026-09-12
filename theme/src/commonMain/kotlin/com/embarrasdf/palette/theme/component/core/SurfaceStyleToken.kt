package com.embarrasdf.palette.theme.component.core

import com.embarrasdf.palette.theme.component.LocalComponentTokens

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.components.core.SurfaceStyle
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.semantic.shape.ShapeToken

enum class SurfaceStyleToken(val default: SurfaceStyleTokenSet) {
    Default(
        SurfaceStyleTokenSet(
            color = ColorToken.Surface,
            shape = ShapeToken.Surface,
            borderStyle = null,
        ),
    ),
    Container(
        SurfaceStyleTokenSet(
            color = ColorToken.Surface,
            shape = ShapeToken.Surface,
            borderStyle = BorderStyleToken.Surface,
        ),
    ),
}

@Composable
fun SurfaceStyleToken.tokenSet(): SurfaceStyleTokenSet =
    LocalComponentTokens.current.surface.getValue(this)

@Composable
fun SurfaceStyleToken.resolve(): SurfaceStyle = tokenSet().toComponentStyle()
