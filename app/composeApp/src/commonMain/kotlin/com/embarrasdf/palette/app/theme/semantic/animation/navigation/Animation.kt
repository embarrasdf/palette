package com.embarrasdf.palette.app.theme.semantic.animation.navigation

import com.embarrasdf.palette.components.layout.catalog.CatalogItem

enum class Animation : CatalogItem {
    Finite,
    Infinite,
    ;

    override val title = this.name
}
