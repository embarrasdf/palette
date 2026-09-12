package com.embarrasdf.palette.app.theme.semantic.color

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.color.ColorDisplay
import com.embarrasdf.palette.components.core.ButtonDefaults
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.semantic.color.PaletteDarkColorScheme
import com.embarrasdf.palette.theme.semantic.color.PaletteLightColorScheme
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.core.Surface
import com.embarrasdf.palette.theme.components.demo.DemoList
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.control.rememberThemeController
import com.embarrasdf.palette.theme.semantic.color.copy
import com.embarrasdf.palette.theme.semantic.color.toColor
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ColorScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberColorScreenState(themeState = themeController)
    val control = rememberColorScreenControl(state = state, themeController = themeController)

    var selectedColorToken by remember { mutableStateOf<ColorToken?>(null) }

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Color",
                onNavigateUp = onNavigateUp,
                onThemeClick = {},
                actions = {},
            )
        },
    ) { paddingValues ->
        DemoList(
            items = ColorToken.entries.toList(),
            key = { it.name },
            controls = control.controls,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { colorToken ->
            ColorDisplayRow(
                label = colorToken.name,
                color = colorToken.toColor(),
                onColorClick = { selectedColorToken = colorToken },
            )
        }
    }

    selectedColorToken?.let { colorToken ->
        ColorPickerDialog(
            colorToken = colorToken,
            onColorSelected = {
                control.onColorSelected(color = it, colorToken = colorToken)
                selectedColorToken = null
            },
            onDismissRequest = {
                selectedColorToken = null
            }
        )
    }
}

@Composable
private fun ColorDisplayRow(
    label: String,
    color: Color,
    onColorClick: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = { onColorClick(color) },
        style = PaletteTheme.component.core.surface.default,
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(PaletteTheme.semantic.dimension.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ColorDisplay(
                color = color,
                style = PaletteTheme.component.color.colorDisplay,
                modifier = Modifier
                    .size(ButtonDefaults.MinHeight)
            )
            Text(
                text = label,
                style = PaletteTheme.component.core.text.labelMedium,
                modifier = Modifier
                    .padding(end = PaletteTheme.semantic.dimension.spacing.medium)
            )
        }
    }
}

@Composable
fun rememberColorScreenState(
    themeState: ThemeState,
): ColorScreenState {
    return rememberSaveable(
        themeState,
        saver = ColorScreenStateSaver(themeState),
    ) {
        ColorScreenState(
            themeState = themeState,
        )
    }
}

@Stable
class ColorScreenState(
    val themeState: ThemeState,
) {
    val isDarkMode: Boolean
        get() = themeState.isDarkMode
}

fun ColorScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        mapOf()
    },
    restore = { map ->
        ColorScreenState(
            themeState = themeState,
        )
    }
)

@Composable
fun rememberColorScreenControl(
    state: ColorScreenState,
    themeController: ThemeController,
): ColorScreenControl {
    return remember(state, themeController) {
        ColorScreenControl(state = state, themeController = themeController)
    }
}

@Stable
class ColorScreenControl(
    val state: ColorScreenState,
    val themeController: ThemeController,
) {
    val isDarkModeControl = Control.Toggle(
        name = "Dark mode",
        value = { state.isDarkMode },
        onValueChange = {
            themeController.setIsDarkMode(it)
        }
    )

    val resetButton = Control.Button(
        name = "Reset",
        onClick = {
            if (themeController.isDarkMode) {
                themeController.updateSemantic {
                    it.copy(colors = it.colors.copy(dark = PaletteDarkColorScheme))
                }
            } else {
                themeController.updateSemantic {
                    it.copy(colors = it.colors.copy(light = PaletteLightColorScheme))
                }
            }
        }
    )

    val controls: PersistentList<Control> = persistentListOf(
        isDarkModeControl,
        resetButton,
    )

    fun onColorSelected(color: Color, colorToken: ColorToken) {
        val colorScheme = themeController.colorScheme.copy(
            token = colorToken,
            color = color,
        )
        if (themeController.isDarkMode) {
            themeController.updateSemantic {
                it.copy(colors = it.colors.copy(dark = colorScheme))
            }
        } else {
            themeController.updateSemantic {
                it.copy(colors = it.colors.copy(light = colorScheme))
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        ColorScreen(
            themeController = rememberThemeController(),
            onNavigateUp = {},
        )
    }
}
