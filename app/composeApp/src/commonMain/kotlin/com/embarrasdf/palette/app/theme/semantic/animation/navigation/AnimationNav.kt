package com.embarrasdf.palette.app.theme.semantic.animation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.navigation.catalogEntry
import com.embarrasdf.palette.app.theme.semantic.animation.FiniteAnimationScreen
import com.embarrasdf.palette.app.theme.semantic.animation.InfiniteAnimationScreen
import com.embarrasdf.palette.navigation.NavController
import com.embarrasdf.palette.navigation.NavGraphBuilder
import com.embarrasdf.palette.navigation.NavKey
import com.embarrasdf.palette.theme.control.ThemeController

fun NavGraphBuilder.animationNavGraph() = navGraph(
    root = AnimationGraph,
    start = AnimationCatalogRoute,
) {
    route(AnimationCatalogRoute)
    route(FiniteRoute)
    route(InfiniteRoute)
}

fun EntryProviderScope<NavKey>.animationEntryProvider(
    navController: NavController,
    themeController: ThemeController,
) {
    catalogEntry<AnimationCatalogRoute, Animation>(
        onItemClick = { item ->
            when (item) {
                Animation.Finite -> navController.navigate(FiniteRoute)
                Animation.Infinite -> navController.navigate(InfiniteRoute)
            }
        },
        title = "Animation",
        onNavigateUp = navController::goBack,
    )

    entry<FiniteRoute> {
        FiniteAnimationScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }

    entry<InfiniteRoute> {
        InfiniteAnimationScreen(
            themeController = themeController,
            onNavigateUp = navController::goBack,
        )
    }
}
