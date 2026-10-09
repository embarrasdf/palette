package com.embarrasdf.palette.app.theme.semantic.motion.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.semantic.motion.InfiniteScreen
import com.embarrasdf.palette.app.theme.semantic.motion.TransitionScreen
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.motionNavGraph() = navGraph(
    root = MotionGraph,
    start = MotionCatalogRoute,
) {
    route(MotionCatalogRoute)
    route(InfiniteRoute)
    route(TransitionRoute)
}

fun EntryProviderScope<NavKey>.motionEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<MotionCatalogRoute, Motion>(
        onItemClick = { item ->
            when (item) {
                Motion.Infinite -> navController.navigate(InfiniteRoute)
                Motion.Transition -> navController.navigate(TransitionRoute)
            }
        },
        title = "Motion",
        onNavigateUp = navController::goBack,
    )

    entry<InfiniteRoute> {
        InfiniteScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    entry<TransitionRoute> {
        TransitionScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }
}
