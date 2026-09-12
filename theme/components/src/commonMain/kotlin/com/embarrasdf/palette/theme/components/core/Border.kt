package com.embarrasdf.palette.theme.components.core

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.core.border
import com.embarrasdf.palette.theme.component.core.BorderStyleToken
import com.embarrasdf.palette.theme.component.core.BorderStyleTokenSet
import com.embarrasdf.palette.theme.component.core.resolve
import com.embarrasdf.palette.theme.semantic.color.toColor
import com.embarrasdf.palette.theme.semantic.shape.toComposeShape

@Composable
fun Modifier.border(
    style: BorderStyleToken,
): Modifier {
    return this.border(style = style.resolve())
}

@Composable
fun Modifier.border(
    style: BorderStyleTokenSet,
): Modifier = this.border(
    width = style.width,
    color = style.color.toColor(),
    shape = style.shape.toComposeShape(),
)
