package com.embarrasdf.palette.theme.components.core

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.core.SurfaceStyle
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.components.core.Surface as BaseSurface

@Composable
fun Surface(
    modifier: Modifier = Modifier,
    style: SurfaceStyle = PaletteTheme.component.core.surface.default,
    content: @Composable (PaddingValues) -> Unit,
) = BaseSurface(
    modifier = modifier,
    style = style,
    content = content,
)

@Composable
fun Surface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: SurfaceStyle = PaletteTheme.component.core.surface.default,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    hapticFeedbackEnabled: Boolean = true,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable (PaddingValues) -> Unit,
) = BaseSurface(
    onClick = onClick,
    modifier = modifier,
    style = style,
    onLongClickLabel = onLongClickLabel,
    onLongClick = onLongClick,
    onDoubleClick = onDoubleClick,
    hapticFeedbackEnabled = hapticFeedbackEnabled,
    enabled = enabled,
    interactionSource = interactionSource,
    content = content,
)
