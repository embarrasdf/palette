package com.embarrasdf.palette.theme.component.money

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.SolidColor
import com.embarrasdf.palette.components.core.TextFieldStyle
import com.embarrasdf.palette.components.core.copy
import com.embarrasdf.palette.components.money.CurrencyAmountFieldStyle
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.core.TextStyles

object MoneyStyles {

    val currencyAmountField: CurrencyAmountFieldStyle
        @Composable get() = CurrencyAmountFieldStyle(
            textFieldStyle = TextFieldStyle(
                textStyle = TextStyles.headline,
                cursorBrush = SolidColor(PaletteTheme.semantic.color.primary),
            ),
            placeholderStyle = TextStyles.headline.copy(
                color = PaletteTheme.semantic.color.primary.copy(alpha = 0.5f),
            ),
            padding = PaddingValues(PaletteTheme.semantic.dimension.spacing.medium),
            spacing = PaletteTheme.semantic.dimension.spacing.small,
        )
}
