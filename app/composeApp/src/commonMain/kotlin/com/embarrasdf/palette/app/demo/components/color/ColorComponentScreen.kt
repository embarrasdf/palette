package com.embarrasdf.palette.app.demo.components.color

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.app.demo.components.color.navigation.ColorComponent
import com.embarrasdf.palette.components.demo.color.ColorPickerDemo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.PaletteTheme

@Composable
fun ColorComponentScreen(
    component: ColorComponent,
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
            ColorComponent.ColorPicker -> ColorPickerDemo(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
