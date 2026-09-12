package com.embarrasdf.palette.theme.components.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.demo.DemoListStyle
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.components.demo.DemoList as BaseDemoList
import kotlinx.collections.immutable.PersistentList

@Composable
fun <T> DemoList(
    items: List<T>,
    controls: PersistentList<Control>,
    modifier: Modifier = Modifier,
    style: DemoListStyle = PaletteTheme.component.demo.list,
    key: ((item: T) -> Any)? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(
        space = 24.dp,
        alignment = Alignment.CenterVertically,
    ),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable DemoScope.(T) -> Unit,
) = BaseDemoList(
    items = items,
    controls = controls,
    modifier = modifier,
    style = style,
    key = key,
    verticalArrangement = verticalArrangement,
    horizontalAlignment = horizontalAlignment,
    content = content,
)
