package com.embarrasdf.palette.app.theme.semantic.format

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.formats.demo.core.TextFormatDemo
import com.embarrasdf.palette.formats.demo.core.TextFormatDemoState
import com.embarrasdf.palette.formats.demo.core.rememberTextFormatDemoControl
import com.embarrasdf.palette.theme.components.demo.DemoList
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.theme.components.layout.BoxWithLabel
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.semantic.format.Formats
import com.embarrasdf.palette.theme.semantic.format.core.TextFormatScheme
import com.embarrasdf.palette.theme.semantic.format.core.TextFormatToken
import com.embarrasdf.palette.theme.semantic.format.core.update
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TextFormatScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberTextFormatScreenState(formats = themeController.semantic.formats)
    val control = rememberTextFormatScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Text",
                onNavigateUp = onNavigateUp,
                onThemeClick = {},
                actions = {},
            )
        },
    ) { paddingValues ->
        DemoList(
            items = state.textFormatsByToken.entries.toList(),
            controls = control.controls,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { (token, _) ->
            BoxWithLabel(
                label = token.name,
            ) {
                TextFormatDemo(
                    state = state.textFormatDemoStatesByToken[token]!!,
                )
            }
        }
    }
}

@Composable
fun rememberTextFormatScreenState(
    formats: Formats,
): TextFormatScreenState {
    return rememberSaveable(
        formats,
        saver = TextFormatScreenStateSaver(formats),
    ) {
        TextFormatScreenState(
            formats = formats,
        )
    }
}

@Stable
class TextFormatScreenState(
    val formats: Formats,
) {
    val textFormatScheme: TextFormatScheme
        get() = formats.textFormats

    val textFormatsByToken = TextFormatToken.entries.associateWith { token ->
        when (token) {
            TextFormatToken.Body -> textFormatScheme.body
            TextFormatToken.Display -> textFormatScheme.display
            TextFormatToken.Headline -> textFormatScheme.headline
            TextFormatToken.Title -> textFormatScheme.title
            TextFormatToken.Label -> textFormatScheme.label
        }
    }

    val textFormatDemoStatesByToken = TextFormatToken.entries.associateWith { token ->
        TextFormatDemoState(
            textFormatInitial = textFormatsByToken[token]!!,
            demoTextFieldState = TextFieldState("Sphinx of black quartz, judge my vow"),
        )
    }
}

fun TextFormatScreenStateSaver(formats: Formats) = mapSaverSafe(
    save = { state ->
        mapOf()
    },
    restore = { map ->
        TextFormatScreenState(
            formats = formats,
        )
    }
)

@Composable
fun rememberTextFormatScreenControl(
    state: TextFormatScreenState,
    themeController: ThemeController,
): TextFormatScreenControl {
    val formatControlByToken = TextFormatToken.entries.associateWith { token ->
        makeControlForToken(
            token = token,
            state = state,
            themeController = themeController,
        )
    }
    return remember(state, themeController) {
        TextFormatScreenControl(
            state = state,
            themeController = themeController,
            formatControlByToken = formatControlByToken,
        )
    }
}

@Stable
class TextFormatScreenControl(
    val state: TextFormatScreenState,
    val themeController: ThemeController,
    formatControlByToken: Map<TextFormatToken, Control>,
) {
    val controls: PersistentList<Control> = persistentListOf(
        *formatControlByToken.values.toTypedArray(),
    )
}

@Composable
private fun makeControlForToken(
    token: TextFormatToken,
    state: TextFormatScreenState,
    themeController: ThemeController,
): Control {
    val demoControl = rememberTextFormatDemoControl(
        state = state.textFormatDemoStatesByToken[token]!!,
        onValueChange = { newValue ->
            themeController.updateSemantic {
                it.copy(
                    formats = state.formats.copy(
                        textFormats = state.textFormatScheme.update(
                            token = token,
                            value = newValue,
                        )
                    )
                )
            }
        }
    )
    return Control.ControlColumn(
        name = token.name,
        controls = { demoControl.controls },
        expandedInitial = false,
        indent = true,
    )
}
