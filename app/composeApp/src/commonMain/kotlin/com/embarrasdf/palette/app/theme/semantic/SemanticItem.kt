package com.embarrasdf.palette.app.theme.semantic

import com.embarrasdf.palette.components.layout.catalog.CatalogItem

enum class SemanticItem : CatalogItem {
    Animation,
    Color,
    Typography,
    Shape,
    Dimension,
    Interaction,
    Format,
    Motion,
    ;

    override val title = name
}
