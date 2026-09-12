package com.embarrasdf.palette.theme.component.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.components.core.Sizing
import com.embarrasdf.palette.components.navigation.BackNavigationButtonStyle
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.core.CoreStyles
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.semantic.color.toColor
import com.embarrasdf.palette.theme.semantic.dimension.SizeToken
import com.embarrasdf.palette.theme.semantic.dimension.toSize
import com.embarrasdf.palette.theme.semantic.shape.ShapeToken
import com.embarrasdf.palette.theme.semantic.shape.toShape

object NavigationStyles {

    val backNavigationButton: BackNavigationButtonStyle
        @Composable get() = BackNavigationButtonStyle(
            buttonStyle = ButtonStyle(
                containerColor = ColorToken.Surface.toColor(),
                shape = ShapeToken.Primary.toShape(),
                contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.none),
                disabledContentAlpha = PaletteTheme.semantic.color.disabledContentAlpha,
                disabledContainerAlpha = PaletteTheme.semantic.color.disabledContainerAlpha,
                indication = PaletteTheme.semantic.indication,
            ),
            iconStyle = CoreStyles.icon.copy(
                size = Sizing.Fixed(SizeToken.IconSmall.toSize()),
                color = PaletteTheme.semantic.color.primary,
            ),
            size = Sizing.Fixed(SizeToken.TouchTargetMin.toSize()),
        )
}
