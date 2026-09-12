package com.embarrasdf.palette.app.demo.components.money.navigation

import com.embarrasdf.palette.components.layout.catalog.CatalogItem
import kotlinx.serialization.Serializable

@Serializable
enum class MoneyComponent : CatalogItem {
    CurrencyAmountField,
    ;

    override val title = this.name
}
