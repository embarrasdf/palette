package com.embarrasdf.palette.theme.component

import androidx.compose.runtime.staticCompositionLocalOf
import com.embarrasdf.palette.theme.component.core.BorderStyleToken
import com.embarrasdf.palette.theme.component.core.BorderStyleTokenSet
import com.embarrasdf.palette.theme.component.core.ButtonStyleToken
import com.embarrasdf.palette.theme.component.core.ButtonStyleTokenSet
import com.embarrasdf.palette.theme.component.core.SurfaceStyleToken
import com.embarrasdf.palette.theme.component.core.SurfaceStyleTokenSet
import com.embarrasdf.palette.theme.component.core.TextStyleToken
import com.embarrasdf.palette.theme.component.core.TextStyleTokenSet

data class ComponentTokens(
    val text: Map<TextStyleToken, TextStyleTokenSet> =
        TextStyleToken.entries.associateWith { it.default },
    val button: Map<ButtonStyleToken, ButtonStyleTokenSet> =
        ButtonStyleToken.entries.associateWith { it.default },
    val surface: Map<SurfaceStyleToken, SurfaceStyleTokenSet> =
        SurfaceStyleToken.entries.associateWith { it.default },
    val border: Map<BorderStyleToken, BorderStyleTokenSet> =
        BorderStyleToken.entries.associateWith { it.default },
)

val LocalComponentTokens = staticCompositionLocalOf { ComponentTokens() }
