package com.embarrasdf.palette.theme.component.menu

import androidx.compose.runtime.Composable
import com.embarrasdf.palette.components.menu.DropdownMenuItemStyle
import com.embarrasdf.palette.components.menu.DropdownMenuStyle
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.core.CoreStyles

object MenuStyles {

    val dropdownMenuItem: DropdownMenuItemStyle
        @Composable get() = DropdownMenuItemStyle(
            indication = PaletteTheme.semantic.indication,
        )

    val dropdownMenu: DropdownMenuStyle
        @Composable get() = DropdownMenuStyle(
            surfaceStyle = CoreStyles.surface.container,
            itemStyle = dropdownMenuItem,
        )
}
