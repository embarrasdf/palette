package com.embarrasdf.palette.app.theme.primitive

import com.embarrasdf.palette.components.layout.catalog.CatalogItem

enum class PrimitiveItem : CatalogItem {
    Typography,
    Shape,
    Interaction,
    ;

    override val title = name
}
