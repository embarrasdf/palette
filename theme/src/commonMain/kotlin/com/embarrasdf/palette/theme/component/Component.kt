package com.embarrasdf.palette.theme.component

import com.embarrasdf.palette.theme.component.auth.AuthStyles
import com.embarrasdf.palette.theme.component.color.ColorStyles
import com.embarrasdf.palette.theme.component.core.CoreStyles
import com.embarrasdf.palette.theme.component.demo.DemoStyles
import com.embarrasdf.palette.theme.component.layout.LayoutStyles
import com.embarrasdf.palette.theme.component.media.MediaStyles
import com.embarrasdf.palette.theme.component.menu.MenuStyles
import com.embarrasdf.palette.theme.component.money.MoneyStyles
import com.embarrasdf.palette.theme.component.navigation.NavigationStyles

object Component {
    val core get() = CoreStyles
    val auth get() = AuthStyles
    val color get() = ColorStyles
    val layout get() = LayoutStyles
    val media get() = MediaStyles
    val menu get() = MenuStyles
    val money get() = MoneyStyles
    val navigation get() = NavigationStyles
    val demo get() = DemoStyles
}
