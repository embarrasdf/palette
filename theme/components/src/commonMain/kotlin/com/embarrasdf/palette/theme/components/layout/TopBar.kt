package com.embarrasdf.palette.theme.components.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.layout.TopBarStyle
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.components.layout.TopBar as BaseTopBar

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    style: TopBarStyle = PaletteTheme.component.layout.topBar,
    navButton: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
) = BaseTopBar(
    modifier = modifier,
    style = style,
    navButton = navButton,
    actions = actions,
    title = title,
)
