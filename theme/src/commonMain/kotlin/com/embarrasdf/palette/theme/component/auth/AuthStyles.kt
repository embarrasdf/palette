package com.embarrasdf.palette.theme.component.auth

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.components.auth.AuthButtonStyle
import com.embarrasdf.palette.components.core.copy
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.core.TextStyles
import com.embarrasdf.palette.theme.component.core.resolve

object AuthButtonStyles {
    val primary: AuthButtonStyle @Composable get() = authButtonStyle(AuthButtonStyleToken.Primary)
    val secondary: AuthButtonStyle @Composable get() = authButtonStyle(AuthButtonStyleToken.Secondary)
    val tertiary: AuthButtonStyle @Composable get() = authButtonStyle(AuthButtonStyleToken.Tertiary)

    @Composable
    operator fun get(token: AuthButtonStyleToken): AuthButtonStyle = authButtonStyle(token)

    @Composable
    private fun authButtonStyle(token: AuthButtonStyleToken): AuthButtonStyle = AuthButtonStyle(
        buttonStyle = token.toButtonStyleToken().resolve(),
        textStyle = when (token) {
            AuthButtonStyleToken.Primary -> TextStyles.labelLarge.copy(
                color = PaletteTheme.semantic.color.onPrimary,
            )
            AuthButtonStyleToken.Secondary -> TextStyles.labelSmall
            AuthButtonStyleToken.Tertiary -> TextStyles.labelSmall
        },
    )
}

object AuthStyles {
    val authButton get() = AuthButtonStyles
}
