package com.embarrasdf.palette.app.theme.semantic

import com.embarrasdf.palette.components.layout.catalog.CatalogItem

enum class SemanticItem : CatalogItem {
    Color,
    Typography,
    Shape,
    Dimension,
    Interaction,
    Format,
    ;

    override val title = name
}
