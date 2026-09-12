package com.embarrasdf.palette.theme.components.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.layout.BoxWithLabelStyle
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.components.layout.BoxWithLabel as BaseBoxWithLabel

@Composable
fun BoxWithLabel(
    label: String,
    modifier: Modifier = Modifier,
    style: BoxWithLabelStyle = PaletteTheme.component.layout.boxWithLabel,
    content: @Composable () -> Unit,
) = BaseBoxWithLabel(
    label = label,
    modifier = modifier,
    style = style,
    content = content,
)
