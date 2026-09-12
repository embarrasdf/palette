package com.embarrasdf.palette.app.demo.components.core

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.app.demo.components.core.navigation.CoreComponent
import com.embarrasdf.palette.components.demo.core.ButtonDemo
import com.embarrasdf.palette.components.demo.core.IconDemo
import com.embarrasdf.palette.components.demo.core.SliderDemo
import com.embarrasdf.palette.components.demo.core.TextDemo
import com.embarrasdf.palette.components.demo.core.TextFieldDemo
import com.embarrasdf.palette.theme.components.layout.Scaffold

@Composable
fun CoreComponentScreen(
    component: CoreComponent,
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
            CoreComponent.Button -> ButtonDemo(
                modifier = Modifier.padding(innerPadding)
            )
            CoreComponent.Icon -> IconDemo(
                modifier = Modifier.padding(innerPadding)
            )
            CoreComponent.Slider -> SliderDemo(
                modifier = Modifier.padding(innerPadding)
            )
            CoreComponent.Text -> TextDemo(
                modifier = Modifier.padding(innerPadding)
            )
            CoreComponent.TextField -> TextFieldDemo(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
