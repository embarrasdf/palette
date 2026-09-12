package com.embarrasdf.palette.app.demo.components.geometry

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.app.demo.components.geometry.navigation.GeometryComponent
import com.embarrasdf.palette.components.demo.geometry.CurveStitchDemo
import com.embarrasdf.palette.components.demo.geometry.GridDemo
import com.embarrasdf.palette.components.demo.geometry.SphereDemo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.PaletteTheme

@Composable
fun GeometryComponentScreen(
    component: GeometryComponent,
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
            GeometryComponent.CurveStitch -> CurveStitchDemo(
                modifier = Modifier.padding(innerPadding)
            )
            GeometryComponent.Grid -> GridDemo(
                modifier = Modifier.padding(innerPadding)
            )
            GeometryComponent.Sphere -> SphereDemo(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
