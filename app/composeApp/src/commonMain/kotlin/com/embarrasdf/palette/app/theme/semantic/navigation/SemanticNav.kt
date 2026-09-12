package com.embarrasdf.palette.app.theme.semantic.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.semantic.SemanticItem
import com.embarrasdf.palette.app.theme.semantic.color.ColorScreen
import com.embarrasdf.palette.app.theme.semantic.dimension.navigation.DimensionGraph
import com.embarrasdf.palette.app.theme.semantic.dimension.navigation.dimensionEntryProvider
import com.embarrasdf.palette.app.theme.semantic.dimension.navigation.dimensionNavGraph
import com.embarrasdf.palette.app.theme.semantic.format.navigation.FormatsGraph
import com.embarrasdf.palette.app.theme.semantic.format.navigation.formatNavGraph
import com.embarrasdf.palette.app.theme.semantic.format.navigation.formatsEntryProvider
import com.embarrasdf.palette.app.theme.semantic.interaction.navigation.InteractionGraph
import com.embarrasdf.palette.app.theme.semantic.interaction.navigation.interactionEntryProvider
import com.embarrasdf.palette.app.theme.semantic.interaction.navigation.interactionNavGraph
import com.embarrasdf.palette.app.theme.semantic.shape.ShapeScreen
import com.embarrasdf.palette.app.theme.semantic.typography.TypographyScreen
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.semanticNavGraph() = navGraph(
    root = SemanticGraph,
    start = SemanticCatalogRoute,
) {
    route(SemanticCatalogRoute)
    route(ColorRoute)
    route(ShapeRoute)
    route(TypographyRoute)
    dimensionNavGraph()
    interactionNavGraph()
    formatNavGraph()
}

fun EntryProviderScope<NavKey>.semanticEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<SemanticCatalogRoute, SemanticItem>(
        onItemClick = { item ->
            when (item) {
                SemanticItem.Color -> navController.navigate(ColorRoute)
                SemanticItem.Typography -> navController.navigate(TypographyRoute)
                SemanticItem.Shape -> navController.navigate(ShapeRoute)
                SemanticItem.Dimension -> navController.navigate(DimensionGraph)
                SemanticItem.Interaction -> navController.navigate(InteractionGraph)
                SemanticItem.Format -> navController.navigate(FormatsGraph)
            }
        },
        title = "Semantic",
        onNavigateUp = navController::goBack,
    )

    entry<ColorRoute> {
        ColorScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    entry<ShapeRoute> {
        ShapeScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    entry<TypographyRoute> {
        TypographyScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    dimensionEntryProvider(navController, themeController)
    interactionEntryProvider(navController, themeController)
    formatsEntryProvider(navController, themeController)
}
