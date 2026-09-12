package com.embarrasdf.palette.theme.components.demo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.DemoStyle
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.components.demo.Demo as BaseDemo
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun Demo(
    modifier: Modifier = Modifier,
    style: DemoStyle = PaletteTheme.component.demo.style,
    controls: PersistentList<Control> = persistentListOf(),
    content: @Composable DemoScope.() -> Unit,
) = BaseDemo(
    modifier = modifier,
    style = style,
    controls = controls,
    content = content,
)
