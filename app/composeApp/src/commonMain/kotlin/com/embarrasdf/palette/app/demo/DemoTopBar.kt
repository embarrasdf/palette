package com.embarrasdf.palette.app.demo

import androidx.compose.foundation.basicMarquee
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.theme.ThemeButton
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.theme.components.layout.TopBar
import com.embarrasdf.palette.theme.components.navigation.BackNavigationButton
import com.embarrasdf.palette.theme.PaletteTheme

@Composable
fun DemoTopBar(
    title: String,
    onNavigateUp: () -> Unit,
    onThemeClick: () -> Unit,
    navButton: @Composable () -> Unit = {
        BackNavigationButton(onNavigateUp)
    },
    actions: @Composable () -> Unit = {
        ThemeButton(onClick = onThemeClick)
    }
) {
    TopBar(
        title = {
            Text(
                text = title,
                style = PaletteTheme.component.core.text.titleMedium,
                modifier = Modifier.basicMarquee(),
            )
        },
        navButton = navButton,
        actions = actions,
    )
}
