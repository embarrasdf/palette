package com.embarrasdf.palette.app.theme.semantic.format.datetime.navigation

import com.embarrasdf.palette.components.layout.catalog.CatalogItem
import kotlinx.serialization.Serializable

@Serializable
enum class DateTimeFormatCatalogItem : CatalogItem {
    Date,
    DateTime,
    Instant,
    Time,
    ;

    override val title = this.name
}
