package com.embarrasdf.palette.app.theme.semantic.format.navigation

import com.embarrasdf.palette.components.layout.catalog.CatalogItem

enum class Format : CatalogItem {
    DateTime,
    Money,
    Number,
    Text,
    ;

    override val title = name
}
