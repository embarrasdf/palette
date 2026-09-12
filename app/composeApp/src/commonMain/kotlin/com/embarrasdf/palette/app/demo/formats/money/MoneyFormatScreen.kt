package com.embarrasdf.palette.app.demo.formats.money

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.app.demo.formats.money.navigation.MoneyFormat
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.formats.demo.money.MoneyFormatDemo
import com.embarrasdf.palette.theme.PaletteTheme

@Composable
fun MoneyFormatScreen(
    format: MoneyFormat,
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
            MoneyFormat.MoneyFormat -> MoneyFormatDemo(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
