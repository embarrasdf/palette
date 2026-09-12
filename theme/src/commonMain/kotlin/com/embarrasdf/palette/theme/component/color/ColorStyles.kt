package com.embarrasdf.palette.theme.component.color

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.embarrasdf.palette.components.color.ColorDisplayStyle
import com.embarrasdf.palette.components.color.ColorPickerControlsStyle
import com.embarrasdf.palette.components.color.ColorPickerDialogContentStyle
import com.embarrasdf.palette.components.color.ColorPickerStyle
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.core.BorderStyleToken
import com.embarrasdf.palette.theme.component.core.CoreStyles
import com.embarrasdf.palette.theme.component.core.TextStyles
import com.embarrasdf.palette.theme.component.core.resolve
import com.embarrasdf.palette.theme.component.layout.LayoutStyles
import com.embarrasdf.palette.theme.semantic.shape.ShapeToken
import com.embarrasdf.palette.theme.semantic.shape.toShape

object ColorStyles {

    val colorDisplay: ColorDisplayStyle
        @Composable get() = ColorDisplayStyle(
            shape = ShapeToken.Primary.toShape(),
            borderStyle = BorderStyleToken.Primary.resolve(),
        )

    val colorPicker: ColorPickerStyle
        @Composable get() = ColorPickerStyle(
            spacing = PaletteTheme.semantic.dimension.spacing.medium,
            colorDisplayStyle = colorDisplay,
            controlsStyle = ColorPickerControlsStyle(
                labelStyle = TextStyles.labelLarge,
                sliderStyle = CoreStyles.slider,
                spacing = PaletteTheme.semantic.dimension.spacing.small,
            ),
        )

    val colorPickerDialogContent: ColorPickerDialogContentStyle
        @Composable get() = ColorPickerDialogContentStyle(
            colorPickerStyle = colorPicker,
            confirmCancelButtonRowStyle = LayoutStyles.confirmCancelButtonRow,
            spacing = PaletteTheme.semantic.dimension.spacing.medium,
            padding = PaddingValues(PaletteTheme.semantic.dimension.spacing.large),
            buttonRowSpacing = PaletteTheme.semantic.dimension.spacing.large,
        )
}
