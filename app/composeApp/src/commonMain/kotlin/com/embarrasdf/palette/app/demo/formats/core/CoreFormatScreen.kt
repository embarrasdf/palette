package com.embarrasdf.palette.app.demo.formats.core

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.app.demo.formats.core.navigation.CoreFormat
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.formats.demo.core.NumberFormatDemo
import com.embarrasdf.palette.formats.demo.core.TextFormatDemo
import com.embarrasdf.palette.theme.PaletteTheme

@Composable
fun CoreFormatScreen(
    format: CoreFormat,
    onNavigateUp: () -> Unit,
    onThemeClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            DemoTopBar(
                title = format.title,
                onNavigateUp = onNavigateUp,
                onThemeClick = onThemeClick,
            )
        },
    ) { innerPadding ->
        when (format) {
            CoreFormat.Number -> NumberFormatDemo(
                modifier = Modifier.padding(innerPadding)
            )
            CoreFormat.Text -> TextFormatDemo(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
