package com.embarrasdf.palette.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.navigation.PaletteNav
import com.embarrasdf.palette.app.navigation.rememberPaletteNavController
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.core.Surface
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.rememberThemeController

@Composable
fun App(
    modifier: Modifier = Modifier,
    navController: NavController = rememberPaletteNavController(),
    themeController: ThemeController = rememberThemeController(),
) {
    PaletteTheme(
        primitive = themeController.primitive,
        semantic = themeController.semantic,
        component = themeController.component,
        isDarkMode = themeController.isDarkMode,
    ) {
        Surface(
            modifier = modifier,
        ) {
            PaletteNav(
                themeController = themeController,
                navController = navController,
            )
        }
    }
}
