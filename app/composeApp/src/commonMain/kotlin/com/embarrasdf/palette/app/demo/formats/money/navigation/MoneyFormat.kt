package com.embarrasdf.palette.app.demo.formats.money.navigation

import com.embarrasdf.palette.components.layout.catalog.CatalogItem
import kotlinx.serialization.Serializable

@Serializable
enum class MoneyFormat : CatalogItem {
    MoneyFormat,
    ;

    override val title = this.name
}
