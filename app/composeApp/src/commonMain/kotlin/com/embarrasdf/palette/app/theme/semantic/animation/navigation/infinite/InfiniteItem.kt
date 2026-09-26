package com.embarrasdf.palette.app.theme.semantic.animation.navigation.infinite

import com.embarrasdf.palette.components.layout.catalog.CatalogItem

/** No infinite animation screens exist yet; the catalog renders empty until one is added. */
enum class InfiniteItem : CatalogItem {
    ;

    override val title: String get() = name
}
