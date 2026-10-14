package com.embarrasdf.palette.app.theme.semantic

import com.embarrasdf.palette.components.layout.catalog.CatalogItem

enum class SemanticItem : CatalogItem {
    Animation,
    Color,
    Dimension,
    Format,
    Interaction,
    Motion,
    Shape,
    Typography,
    ;

    override val title = name
}
