package com.embarrasdf.palette.app.theme.component.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.component.core.BorderStyleScreen
import com.embarrasdf.palette.app.theme.component.core.ButtonStyleScreen
import com.embarrasdf.palette.app.theme.component.core.CoreStyleItem
import com.embarrasdf.palette.app.theme.component.core.SurfaceStyleScreen
import com.embarrasdf.palette.app.theme.component.core.TextStyleScreen
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.coreNavGraph() = navGraph(
    root = CoreGraph,
    start = CoreCatalogRoute,
) {
    route(CoreCatalogRoute)
    route(ButtonStylesRoute)
    route(BorderStylesRoute)
    route(SurfaceStylesRoute)
    route(TextStylesRoute)
}

fun EntryProviderScope<NavKey>.coreEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<CoreCatalogRoute, CoreStyleItem>(
        onItemClick = { style ->
            when (style) {
                CoreStyleItem.Border -> navController.navigate(BorderStylesRoute)
                CoreStyleItem.Button -> navController.navigate(ButtonStylesRoute)
                CoreStyleItem.Surface -> navController.navigate(SurfaceStylesRoute)
                CoreStyleItem.Text -> navController.navigate(TextStylesRoute)
            }
        },
        title = "Core",
        onNavigateUp = navController::goBack,
    )

    entry<BorderStylesRoute> {
        BorderStyleScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    entry<ButtonStylesRoute> {
        ButtonStyleScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    entry<SurfaceStylesRoute> {
        SurfaceStyleScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    entry<TextStylesRoute> {
        TextStyleScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }
}
