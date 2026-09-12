package com.embarrasdf.palette.components.menu

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.PopupProperties

@Composable
fun ContextMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    offset: Offset = Offset.Zero,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    style: DropdownMenuStyle = DropdownMenuStyle(),
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalDensity.current
    Box(
        modifier = Modifier.absoluteOffset(
            x = with(density) { offset.x.toDp() },
            y = with(density) { offset.y.toDp() },
        )
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            scrollState = scrollState,
            style = style,
            properties = properties,
            content = content,
        )
    }
}
