package com.embarrasdf.palette.app.theme.primitive

import com.embarrasdf.palette.components.layout.catalog.CatalogItem

enum class PrimitiveItem : CatalogItem {
    Animation,
    Interaction,
    Shape,
    Typography,
    ;

    override val title = name
}
