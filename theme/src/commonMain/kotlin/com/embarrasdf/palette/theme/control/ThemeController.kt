package com.embarrasdf.palette.theme.control

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.theme.component.ComponentTokens
import com.embarrasdf.palette.theme.primitive.PrimitiveTokens
import com.embarrasdf.palette.theme.semantic.SemanticTokens

class ThemeController internal constructor(
    private val state: ThemeStateImpl
): ThemeState by state {

    fun updatePrimitive(block: (PrimitiveTokens) -> PrimitiveTokens) {
        state.primitive = block(state.primitive)
    }

    fun updateSemantic(block: (SemanticTokens) -> SemanticTokens) {
        state.semantic = block(state.semantic)
    }

    fun updateComponent(block: (ComponentTokens) -> ComponentTokens) {
        state.component = block(state.component)
    }

    fun setIsDarkMode(isDarkMode: Boolean) {
        state.isDarkMode = isDarkMode
    }
}

@Composable
fun rememberThemeController(): ThemeController {
    val state: ThemeStateImpl = rememberThemeState()
    return ThemeController(state)
}
