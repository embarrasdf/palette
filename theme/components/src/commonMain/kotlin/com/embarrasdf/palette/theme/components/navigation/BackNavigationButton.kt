package com.embarrasdf.palette.theme.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.navigation.BackNavigationButtonStyle
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.components.navigation.BackNavigationButton as BaseBackNavigationButton

@Composable
fun BackNavigationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: BackNavigationButtonStyle = PaletteTheme.component.navigation.backNavigationButton,
) = BaseBackNavigationButton(
    onClick = onClick,
    modifier = modifier,
    style = style,
)
