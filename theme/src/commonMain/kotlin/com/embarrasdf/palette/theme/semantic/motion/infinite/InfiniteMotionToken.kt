package com.embarrasdf.palette.theme.semantic.motion.infinite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.theme.PaletteTheme

enum class InfiniteMotionToken {
    Loading,
}

fun InfiniteMotionToken.toEffect(infiniteMotionScheme: InfiniteMotionScheme): InfiniteEffect {
    return when (this) {
        InfiniteMotionToken.Loading -> infiniteMotionScheme.loading
    }
}

@Composable
fun InfiniteMotionToken.toEffect(): InfiniteEffect {
    return toEffect(PaletteTheme.semantic.motion.infinite)
}

@Composable
fun InfiniteMotionToken.toModifier(): Modifier {
    return toEffect().toModifier()
}
