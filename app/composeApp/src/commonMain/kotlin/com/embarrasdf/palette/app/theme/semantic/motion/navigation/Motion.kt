package com.embarrasdf.palette.app.theme.semantic.motion.navigation

import com.embarrasdf.palette.components.layout.catalog.CatalogItem

enum class Motion : CatalogItem {
    Infinite,
    Transition,
    ;

    override val title = this.name
}
