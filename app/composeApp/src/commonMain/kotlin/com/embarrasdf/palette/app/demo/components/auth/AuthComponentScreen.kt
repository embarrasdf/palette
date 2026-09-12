package com.embarrasdf.palette.app.demo.components.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.app.demo.components.auth.navigation.AuthComponent
import com.embarrasdf.palette.components.demo.auth.AuthButtonDemo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.PaletteTheme

@Composable
fun AuthComponentScreen(
    component: AuthComponent,
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
            AuthComponent.Button -> AuthButtonDemo(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
