package com.embarrasdf.palette.app.theme.primitive.typography

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.demo.core.ComposeTextStyleDemoDefault
import com.embarrasdf.palette.components.demo.core.ComposeTextStyleDemoState
import com.embarrasdf.palette.components.demo.core.TextDemo
import com.embarrasdf.palette.components.demo.core.TextDemoControl
import com.embarrasdf.palette.components.demo.core.TextDemoState
import com.embarrasdf.palette.components.demo.core.TextStyleDemoDefault
import com.embarrasdf.palette.components.demo.core.rememberTextDemoControl
import com.embarrasdf.palette.components.demo.core.rememberTextDemoState
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.rememberThemeController
import com.embarrasdf.palette.theme.primitive.FontFamily
import com.embarrasdf.palette.theme.primitive.FontStyle
import com.embarrasdf.palette.theme.primitive.FontWeight
import com.embarrasdf.palette.theme.primitive.toComposeFontFamily
import com.embarrasdf.palette.theme.primitive.toComposeFontStyle
import com.embarrasdf.palette.theme.primitive.toComposeFontWeight
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TypographyScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberTypographyScreenState()
    val control = rememberTypographyScreenControl(state = state)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Typography",
                onNavigateUp = onNavigateUp,
                onThemeClick = {},
                actions = {},
            )
        },
    ) { paddingValues ->
        Demo(
            controls = control.controls,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextDemo(
                state = state.textDemoState,
                control = control.textDemoControl,
            )
        }
    }
}

@Composable
fun rememberTypographyScreenState(): TypographyScreenState {
    val textDemoState = rememberTextDemoState(
        textStyleInitial = TextStyleDemoDefault.copy(
            composeTextStyle = ComposeTextStyleDemoDefault.copy(
                fontFamily = FontFamily.Monospace.toComposeFontFamily(),
                fontWeight = FontWeight.Normal.toComposeFontWeight(),
                fontStyle = FontStyle.Normal.toComposeFontStyle(),
            ),
        ),
    )
    return remember(textDemoState) {
        TypographyScreenState(
            textDemoState = textDemoState,
        )
    }
}

@Stable
class TypographyScreenState(
    val textDemoState: TextDemoState,
) {
    val composeTextStyleState: ComposeTextStyleDemoState
        get() = textDemoState.textStyleDemoState.composeTextStyleDemoState
}

@Composable
fun rememberTypographyScreenControl(
    state: TypographyScreenState,
): TypographyScreenControl {
    val textDemoControl = rememberTextDemoControl(state.textDemoState)
    return remember(state, textDemoControl) {
        TypographyScreenControl(
            state = state,
            textDemoControl = textDemoControl,
        )
    }
}

@Stable
class TypographyScreenControl(
    val state: TypographyScreenState,
    val textDemoControl: TextDemoControl,
) {
    private val fontFamilyControl = enumControl(
        name = "Font family",
        values = { FontFamily.entries },
        selectedValue = { state.composeTextStyleState.fontFamily ?: FontFamily.Default },
        onValueChange = { fontFamily ->
            updateComposeTextStyle { it.copy(fontFamily = fontFamily.toComposeFontFamily()) }
        },
    )

    private val fontWeightControl = enumControl(
        name = "Font weight",
        values = { FontWeight.entries },
        selectedValue = { state.composeTextStyleState.fontWeight ?: FontWeight.Normal },
        onValueChange = { fontWeight ->
            updateComposeTextStyle { it.copy(fontWeight = fontWeight.toComposeFontWeight()) }
        },
    )

    private val fontStyleControl = enumControl(
        name = "Font style",
        values = { FontStyle.entries },
        selectedValue = { state.composeTextStyleState.fontStyle ?: FontStyle.Normal },
        onValueChange = { fontStyle ->
            updateComposeTextStyle { it.copy(fontStyle = fontStyle.toComposeFontStyle()) }
        },
    )

    private val textControl = Control.ControlColumn(
        name = "Text",
        controls = { textDemoControl.controls },
    )

    val controls: PersistentList<Control> = persistentListOf(
        fontFamilyControl,
        fontWeightControl,
        fontStyleControl,
        textControl,
    )

    private fun updateComposeTextStyle(
        transform: (androidx.compose.ui.text.TextStyle) -> androidx.compose.ui.text.TextStyle,
    ) {
        textDemoControl.updateTextStyle(
            textStyle = with(state.textDemoState.textStyleDemoState.textStyle) {
                copy(composeTextStyle = transform(composeTextStyle))
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        TypographyScreen(
            themeController = rememberThemeController(),
            onNavigateUp = {},
        )
    }
}
