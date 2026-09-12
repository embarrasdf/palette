package com.embarrasdf.palette.app.demo.components.money

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.app.demo.components.money.navigation.MoneyComponent
import com.embarrasdf.palette.components.demo.money.CurrencyAmountFieldDemo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.PaletteTheme

@Composable
fun MoneyComponentScreen(
    component: MoneyComponent,
    onNavigateUp: () -> Unit,
    onThemeClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            DemoTopBar(
                title = component.title,
                onNavigateUp = onNavigateUp,
                onThemeClick = onThemeClick,
            )
        },
    ) { innerPadding ->
        when (component) {
            MoneyComponent.CurrencyAmountField -> CurrencyAmountFieldDemo(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
