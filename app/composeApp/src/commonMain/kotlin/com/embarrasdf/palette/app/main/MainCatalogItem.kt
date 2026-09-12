package com.embarrasdf.palette.app.main

import com.embarrasdf.palette.components.layout.catalog.CatalogItem

enum class MainCatalogItem : CatalogItem {
    Components,
    Formats,
    Modifiers;

    override val title = this.name
}
