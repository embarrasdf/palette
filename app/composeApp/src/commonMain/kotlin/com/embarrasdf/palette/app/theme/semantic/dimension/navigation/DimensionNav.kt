package com.embarrasdf.palette.app.theme.semantic.dimension.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.semantic.dimension.PaddingScreen
import com.embarrasdf.palette.app.theme.semantic.dimension.SizeScreen
import com.embarrasdf.palette.app.theme.semantic.dimension.SpacingScreen
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.dimensionNavGraph() = navGraph(
    root = DimensionGraph,
    start = DimensionCatalogRoute,
) {
    route(DimensionCatalogRoute)
    route(SpacingRoute)
    route(PaddingRoute)
    route(SizeRoute)
}

fun EntryProviderScope<NavKey>.dimensionEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<DimensionCatalogRoute, Dimension>(
        onItemClick = { dimension ->
            when (dimension) {
                Dimension.Spacing -> navController.navigate(SpacingRoute)
                Dimension.Padding -> navController.navigate(PaddingRoute)
                Dimension.Size -> navController.navigate(SizeRoute)
            }
        },
        title = "Dimension",
        onNavigateUp = navController::goBack,
    )

    entry<SpacingRoute> {
        SpacingScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    entry<PaddingRoute> {
        PaddingScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    entry<SizeRoute> {
        SizeScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }
}
