package com.embarrasdf.palette.theme.component.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.components.core.CheckboxStyle
import com.embarrasdf.palette.components.core.ChevronButtonStyle
import com.embarrasdf.palette.components.core.DividerStyle
import com.embarrasdf.palette.components.core.Sizing
import com.embarrasdf.palette.components.core.IconStyle
import com.embarrasdf.palette.components.core.ProgressIndicatorStyle
import com.embarrasdf.palette.components.core.SliderColors
import com.embarrasdf.palette.components.core.SliderStyle
import com.embarrasdf.palette.components.core.TextFieldStyle
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.semantic.color.toColor
import com.embarrasdf.palette.theme.semantic.dimension.SizeToken
import com.embarrasdf.palette.theme.semantic.dimension.toSize
import com.embarrasdf.palette.theme.semantic.shape.ShapeToken
import com.embarrasdf.palette.theme.semantic.shape.toShape

object CoreStyles {
    val text get() = TextStyles

    val button get() = ButtonStyles

    val border get() = BorderStyles

    val surface get() = SurfaceStyles

    val divider: DividerStyle
        @Composable get() = DividerStyle(
            color = PaletteTheme.semantic.color.outline,
        )

    val progressIndicator: ProgressIndicatorStyle
        @Composable get() = ProgressIndicatorStyle(
            textStyle = text.bodyMedium,
        )

    val checkbox: CheckboxStyle
        @Composable get() = CheckboxStyle(
            buttonStyle = ButtonStyle(
                containerColor = ColorToken.Surface.toColor(),
                disabledContentAlpha = PaletteTheme.semantic.color.disabledContentAlpha,
                disabledContainerAlpha = PaletteTheme.semantic.color.disabledContainerAlpha,
                indication = PaletteTheme.semantic.indication,
            ),
            textStyle = text.titleLarge,
        )

    val icon: IconStyle
        @Composable get() = IconStyle(
            size = Sizing.Fixed(SizeToken.IconSmall.toSize()),
            color = PaletteTheme.semantic.color.primary,
        )

    val chevronButton: ChevronButtonStyle
        @Composable get() = ChevronButtonStyle(
            buttonStyle = ButtonStyle(
                containerColor = ColorToken.Surface.toColor(),
                shape = ShapeToken.Primary.toShape(),
                contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.medium),
                disabledContentAlpha = PaletteTheme.semantic.color.disabledContentAlpha,
                disabledContainerAlpha = PaletteTheme.semantic.color.disabledContainerAlpha,
                indication = PaletteTheme.semantic.indication,
            ),
            iconStyle = icon,
        )

    val slider: SliderStyle
        @Composable get() = SliderStyle(
            colors = SliderColors(
                trackColor = PaletteTheme.semantic.color.primary,
                thumbColor = PaletteTheme.semantic.color.primary,
                thumbPointColor = PaletteTheme.semantic.color.primary,
                thumbBackgroundColor = PaletteTheme.semantic.color.surface,
            ),
        )

    val textField: TextFieldStyle
        @Composable get() = TextFieldStyle(
            textStyle = text.bodyMedium,
            cursorBrush = SolidColor(PaletteTheme.semantic.color.primary),
            borderStroke = BorderStroke(1.dp, PaletteTheme.semantic.color.outline),
            contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.small),
        )
}
