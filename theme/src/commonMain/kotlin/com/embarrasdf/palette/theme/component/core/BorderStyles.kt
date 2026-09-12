package com.embarrasdf.palette.theme.component.core

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.components.core.BorderStyle

object BorderStyles {
    val surface: BorderStyle @Composable get() = BorderStyleToken.Surface.resolve()
    val primary: BorderStyle @Composable get() = BorderStyleToken.Primary.resolve()
    val secondary: BorderStyle @Composable get() = BorderStyleToken.Secondary.resolve()
    val tertiary: BorderStyle @Composable get() = BorderStyleToken.Tertiary.resolve()

    @Composable
    operator fun get(token: BorderStyleToken): BorderStyle = token.resolve()
}
