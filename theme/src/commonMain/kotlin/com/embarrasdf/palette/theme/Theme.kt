package com.embarrasdf.palette.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.embarrasdf.palette.theme.component.Component
import com.embarrasdf.palette.theme.component.ComponentTokens
import com.embarrasdf.palette.theme.component.LocalComponentTokens
import com.embarrasdf.palette.theme.primitive.LocalPrimitiveTokens
import com.embarrasdf.palette.theme.primitive.Primitive
import com.embarrasdf.palette.theme.primitive.PrimitiveTokens
import com.embarrasdf.palette.theme.semantic.LocalIsDarkMode
import com.embarrasdf.palette.theme.semantic.LocalSemanticTokens
import com.embarrasdf.palette.theme.semantic.Semantic
import com.embarrasdf.palette.theme.semantic.SemanticTokens

@Composable
fun PaletteTheme(
    primitive: PrimitiveTokens = PrimitiveTokens(),
    semantic: SemanticTokens = SemanticTokens(),
    component: ComponentTokens = ComponentTokens(),
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalPrimitiveTokens provides primitive,
        LocalSemanticTokens provides semantic,
        LocalComponentTokens provides component,
        LocalIsDarkMode provides isDarkMode,
        content = content,
    )
}

object PaletteTheme {
    val primitive get() = Primitive
    val semantic get() = Semantic
    val component get() = Component
}
