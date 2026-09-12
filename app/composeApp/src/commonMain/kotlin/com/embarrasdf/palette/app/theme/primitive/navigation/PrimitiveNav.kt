package com.embarrasdf.palette.app.theme.primitive.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.primitive.PrimitiveItem
import com.embarrasdf.palette.app.theme.primitive.interaction.navigation.PrimitiveInteractionGraph
import com.embarrasdf.palette.app.theme.primitive.interaction.navigation.primitiveInteractionEntryProvider
import com.embarrasdf.palette.app.theme.primitive.interaction.navigation.primitiveInteractionNavGraph
import com.embarrasdf.palette.app.theme.primitive.shape.ShapeScreen
import com.embarrasdf.palette.app.theme.primitive.typography.TypographyScreen
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.primitiveNavGraph() = navGraph(
    root = PrimitiveGraph,
    start = PrimitiveCatalogRoute,
) {
    route(PrimitiveCatalogRoute)
    route(PrimitiveTypographyRoute)
    route(PrimitiveShapeRoute)
    primitiveInteractionNavGraph()
}

fun EntryProviderScope<NavKey>.primitiveEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<PrimitiveCatalogRoute, PrimitiveItem>(
        onItemClick = { item ->
            when (item) {
                PrimitiveItem.Typography -> navController.navigate(PrimitiveTypographyRoute)
                PrimitiveItem.Shape -> navController.navigate(PrimitiveShapeRoute)
                PrimitiveItem.Interaction -> navController.navigate(PrimitiveInteractionGraph)
            }
        },
        title = "Primitive",
        onNavigateUp = navController::goBack,
    )

    entry<PrimitiveTypographyRoute> {
        TypographyScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    entry<PrimitiveShapeRoute> {
        ShapeScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    primitiveInteractionEntryProvider(navController, themeController)
}
