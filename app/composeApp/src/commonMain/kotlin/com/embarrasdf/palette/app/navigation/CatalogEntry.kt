package com.embarrasdf.palette.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import com.embarrasdf.palette.app.catalog.CatalogScreen
import com.embarrasdf.palette.components.layout.catalog.CatalogItem
import com.embarrasdf.palette.navigation.NavKey

inline fun <reified T : NavKey, reified E> EntryProviderScope<NavKey>.catalogEntry(
    noinline onItemClick: (E) -> Unit,
    title: String? = null,
    noinline onNavigateUp: (() -> Unit)? = null,
    noinline actions: @Composable () -> Unit = {}
) where E : Enum<E>, E : CatalogItem {
    entry<T> {
        CatalogScreen(
            items = enumValues<E>().toList(),
            onItemClick = onItemClick,
            title = title,
            onNavigateUp = onNavigateUp,
            actions = actions,
        )
    }
}
