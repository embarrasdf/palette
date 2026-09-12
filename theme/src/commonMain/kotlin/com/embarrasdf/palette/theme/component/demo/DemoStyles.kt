package com.embarrasdf.palette.theme.component.demo

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.embarrasdf.palette.components.core.Sizing
import com.embarrasdf.palette.components.demo.DemoListStyle
import com.embarrasdf.palette.components.demo.DemoStyle
import com.embarrasdf.palette.components.demo.control.ButtonControlStyle
import com.embarrasdf.palette.components.demo.control.CharControlStyle
import com.embarrasdf.palette.components.demo.control.ColorControlStyle
import com.embarrasdf.palette.components.demo.control.ControlsStyle
import com.embarrasdf.palette.components.demo.control.DropdownControlStyle
import com.embarrasdf.palette.components.demo.control.DynamicListControlStyle
import com.embarrasdf.palette.components.demo.control.ExpandableHeaderStyle
import com.embarrasdf.palette.components.demo.control.SliderControlStyle
import com.embarrasdf.palette.components.demo.control.TextFieldControlStyle
import com.embarrasdf.palette.components.demo.control.ToggleControlStyle
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.color.ColorStyles
import com.embarrasdf.palette.theme.component.core.CoreStyles
import com.embarrasdf.palette.theme.component.core.TextStyles
import com.embarrasdf.palette.theme.component.menu.MenuStyles

object DemoStyles {

    val list: DemoListStyle
        @Composable get() = DemoListStyle(
            demoStyle = style,
            itemSpacing = PaletteTheme.semantic.dimension.spacing.large,
            contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.medium),
        )

    val style: DemoStyle
        @Composable get() {
            val label = TextStyles.labelLarge
            val button = CoreStyles.button.secondary
            val textField = CoreStyles.textField
            return DemoStyle(
                dividerStyle = CoreStyles.divider,
                contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.medium),
                controlsStyle = ControlsStyle(
                    spacing = PaletteTheme.semantic.dimension.spacing.medium,
                    verticalContentPadding = PaletteTheme.semantic.dimension.spacing.small,
                    horizontalContentPadding = PaletteTheme.semantic.dimension.spacing.small,
                    rowSpacing = PaletteTheme.semantic.dimension.spacing.small,
                    indent = PaletteTheme.semantic.dimension.spacing.medium,
                    expandableHeader = ExpandableHeaderStyle(
                        headerStyle = TextStyles.labelSmall,
                        borderColor = PaletteTheme.semantic.color.outline,
                        chevronIconStyle = CoreStyles.icon.copy(
                            size = Sizing.Scale(0.4f),
                        ),
                        chevronPadding = PaddingValues(all = PaletteTheme.semantic.dimension.spacing.none),
                        spacing = PaletteTheme.semantic.dimension.spacing.small,
                        labelPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.xs),
                        indication = PaletteTheme.semantic.indication,
                    ),
                    button = ButtonControlStyle(
                        labelStyle = label,
                        buttonStyle = button,
                    ),
                    slider = SliderControlStyle(
                        labelStyle = label,
                        sliderStyle = CoreStyles.slider,
                        spacing = PaletteTheme.semantic.dimension.spacing.small,
                    ),
                    color = ColorControlStyle(
                        labelStyle = label,
                        buttonStyle = button,
                        colorDisplayStyle = ColorStyles.colorDisplay,
                        colorPickerDialogContentStyle = ColorStyles.colorPickerDialogContent,
                        surfaceStyle = CoreStyles.surface.container,
                        spacing = PaletteTheme.semantic.dimension.spacing.medium,
                        contentSpacing = PaletteTheme.semantic.dimension.spacing.small,
                    ),
                    toggle = ToggleControlStyle(
                        labelStyle = label,
                        checkboxStyle = CoreStyles.checkbox,
                        spacing = PaletteTheme.semantic.dimension.spacing.small,
                    ),
                    char = CharControlStyle(
                        labelStyle = label,
                        textFieldStyle = textField,
                        spacing = PaletteTheme.semantic.dimension.spacing.small,
                        contentPadding = PaddingValues(vertical = PaletteTheme.semantic.dimension.spacing.small),
                    ),
                    textField = TextFieldControlStyle(
                        labelStyle = label,
                        textFieldStyle = textField,
                        spacing = PaletteTheme.semantic.dimension.spacing.small,
                        contentPadding = PaddingValues(vertical = PaletteTheme.semantic.dimension.spacing.small),
                    ),
                    dropdown = DropdownControlStyle(
                        labelStyle = label,
                        buttonStyle = button,
                        menuStyle = MenuStyles.dropdownMenu,
                        labelSpacing = PaletteTheme.semantic.dimension.spacing.small,
                        rowSpacing = PaletteTheme.semantic.dimension.spacing.medium,
                    ),
                    dynamicList = DynamicListControlStyle(
                        spacing = PaletteTheme.semantic.dimension.spacing.medium,
                        itemSpacing = PaletteTheme.semantic.dimension.spacing.small,
                        itemControlSpacing = PaletteTheme.semantic.dimension.spacing.xs,
                        indent = PaletteTheme.semantic.dimension.spacing.medium,
                    ),
                ),
            )
        }
}
