package com.embarrasdf.palette.theme.component.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import com.embarrasdf.palette.components.layout.BoxWithLabelStyle
import com.embarrasdf.palette.components.layout.FloatingActionStyle
import com.embarrasdf.palette.components.layout.ScaffoldStyle
import com.embarrasdf.palette.components.layout.TopBarStyle
import com.embarrasdf.palette.components.layout.catalog.CatalogStyle
import com.embarrasdf.palette.components.layout.dialog.ConfirmButtonStyle
import com.embarrasdf.palette.components.layout.dialog.ConfirmCancelButtonRowStyle
import com.embarrasdf.palette.components.layout.dialog.DialogContentStyle
import com.embarrasdf.palette.components.layout.dialog.ProgressDialogContentStyle
import com.embarrasdf.palette.components.core.copy
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.core.CoreStyles
import com.embarrasdf.palette.theme.component.core.TextStyles
import com.embarrasdf.palette.theme.semantic.color.toColor
import com.embarrasdf.palette.theme.semantic.dimension.SizeToken
import com.embarrasdf.palette.theme.semantic.dimension.toSize

object LayoutStyles {

    val scaffold: ScaffoldStyle
        @Composable get() = ScaffoldStyle(
            surfaceStyle = CoreStyles.surface.default,
        )

    val floatingAction: FloatingActionStyle
        @Composable get() = FloatingActionStyle(
            spacing = PaletteTheme.semantic.dimension.spacing.small,
        )

    val topBar: TopBarStyle
        @Composable get() = TopBarStyle(
            spacing = PaletteTheme.semantic.dimension.spacing.small,
            minHeight = SizeToken.TouchTargetMin.toSize(),
        )

    val boxWithLabel: BoxWithLabelStyle
        @Composable get() = BoxWithLabelStyle(
            spacing = PaletteTheme.semantic.dimension.spacing.small,
            labelPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.xs),
            labelStyle = TextStyles.labelSmall,
            borderColor = PaletteTheme.semantic.color.outline,
        )

    val catalog: CatalogStyle
        @Composable get() = CatalogStyle(
            itemSpacing = PaletteTheme.semantic.dimension.spacing.medium,
            itemStyle = CoreStyles.button.secondary,
            itemTextStyle = TextStyles.bodyMedium.copy(
                color = ColorToken.Secondary.toColor(),
            ),
        )

    val confirmCancelButtonRow: ConfirmCancelButtonRowStyle
        @Composable get() = ConfirmCancelButtonRowStyle(
            buttonStyle = ConfirmButtonStyle(
                buttonStyle = CoreStyles.button.secondary,
                textStyle = TextStyles.bodyMedium.copy(
                    color = ColorToken.Secondary.toColor(),
                ),
            ),
            spacing = PaletteTheme.semantic.dimension.spacing.medium,
        )

    val dialogContent: DialogContentStyle
        @Composable get() = DialogContentStyle(
            titleStyle = TextStyles.titleLarge.copy(textAlign = TextAlign.Center),
            messageStyle = TextStyles.bodyLarge.copy(textAlign = TextAlign.Center),
            surfaceStyle = CoreStyles.surface.container,
            buttonRowStyle = confirmCancelButtonRow,
            spacing = PaletteTheme.semantic.dimension.spacing.medium,
            padding = PaddingValues(PaletteTheme.semantic.dimension.spacing.large),
            titlePadding = PaddingValues(bottom = PaletteTheme.semantic.dimension.spacing.medium),
            messagePadding = PaddingValues(bottom = PaletteTheme.semantic.dimension.spacing.large),
        )

    val progressDialogContent: ProgressDialogContentStyle
        @Composable get() = ProgressDialogContentStyle(
            dialogContentStyle = dialogContent,
            progressIndicatorStyle = CoreStyles.progressIndicator,
        )
}
