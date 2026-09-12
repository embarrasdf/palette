package com.embarrasdf.palette.app.theme

import com.embarrasdf.palette.components.layout.catalog.CatalogItem

enum class ThemeItem : CatalogItem {
    Primitive,
    Semantic,
    Component,
    ;

    override val title = this.name
}
