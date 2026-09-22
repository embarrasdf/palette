package com.embarrasdf.palette.app.theme.semantic.animation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.semantic.animation.TransitionScreen
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.animationNavGraph() = navGraph(
    root = AnimationGraph,
    start = AnimationCatalogRoute,
) {
    route(AnimationCatalogRoute)
    route(TransitionRoute)
}

fun EntryProviderScope<NavKey>.animationEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<AnimationCatalogRoute, Animation>(
        onItemClick = { item ->
            when (item) {
                Animation.Transition -> navController.navigate(TransitionRoute)
            }
        },
        title = "Animation",
        onNavigateUp = navController::goBack,
    )

    entry<TransitionRoute> {
        TransitionScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }
}
