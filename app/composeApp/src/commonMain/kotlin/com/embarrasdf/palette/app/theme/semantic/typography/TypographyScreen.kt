package com.embarrasdf.palette.app.theme.semantic.typography

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.formats.core.TextFormat
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.demo.DemoList
import com.embarrasdf.palette.theme.components.layout.BoxWithLabel
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.primitive.FontFamily
import com.embarrasdf.palette.theme.primitive.FontStyle
import com.embarrasdf.palette.theme.primitive.FontWeight
import com.embarrasdf.palette.theme.semantic.typography.TypographyToken
import com.embarrasdf.palette.theme.semantic.typography.TypographyTokenSet
import com.embarrasdf.palette.theme.semantic.typography.toComposeTextStyle
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TypographyScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberTypographyScreenState(themeState = themeController)
    val control = rememberTypographyScreenControl(state = state, themeController = themeController)

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
        DemoList(
            items = TypographyToken.entries.toList(),
            controls = control.controls,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { token ->
            BoxWithLabel(
                label = token.name,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = state.text,
                    style = TextStyle(
                        composeTextStyle = token.toComposeTextStyle().copy(
                            color = PaletteTheme.semantic.color.onSurface,
                        ),
                        format = TextFormat(),
                    ),
                )
            }
        }
    }
}

@Composable
fun rememberTypographyScreenState(
    themeState: ThemeState,
    initialText: String = "Sphinx of black quartz, judge my vow",
): TypographyScreenState {
    val textFieldState = rememberTextFieldState(initialText = initialText)
    return rememberSaveable(
        themeState,
        saver = TypographyScreenStateSaver(themeState),
    ) {
        TypographyScreenState(
            themeState = themeState,
            textFieldState = textFieldState,
        )
    }
}

@Stable
class TypographyScreenState(
    val themeState: ThemeState,
    val textFieldState: TextFieldState,
) {
    val text: String
        get() = textFieldState.text.toString()

    fun tokenSet(token: TypographyToken): TypographyTokenSet =
        themeState.semantic.typography.tokens.getValue(token)
}

private val textFieldKey = "textField"

fun TypographyScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        mapOf(
            textFieldKey to save(state.textFieldState, TextFieldState.Saver, this),
        )
    },
    restore = { map ->
        TypographyScreenState(
            themeState = themeState,
            textFieldState = restore(map[textFieldKey], TextFieldState.Saver)!!,
        )
    }
)

@Composable
fun rememberTypographyScreenControl(
    state: TypographyScreenState,
    themeController: ThemeController,
): TypographyScreenControl {
    return remember(state, themeController) {
        TypographyScreenControl(
            state = state,
            themeController = themeController,
        )
    }
}

@Stable
class TypographyScreenControl(
    val state: TypographyScreenState,
    val themeController: ThemeController,
) {
    private val textFieldControl = Control.TextField(
        name = "Sample text",
        includeLabel = false,
        textFieldState = state.textFieldState,
    )

    private val tokenControls: List<Control.ControlColumn> =
        TypographyToken.entries.map { makeControlForToken(it) }

    val controls: PersistentList<Control> = persistentListOf(
        textFieldControl,
        *tokenControls.toTypedArray(),
    )

    private fun makeControlForToken(token: TypographyToken): Control.ControlColumn {
        val fontFamilyControl = enumControl(
            name = "Font family",
            values = { FontFamily.entries },
            selectedValue = { state.tokenSet(token).fontFamily },
            onValueChange = { fontFamily ->
                updateTokenSet(token) { it.copy(fontFamily = fontFamily) }
            },
        )
        val fontWeightControl = enumControl(
            name = "Font weight",
            values = { FontWeight.entries },
            selectedValue = { state.tokenSet(token).fontWeight },
            onValueChange = { fontWeight ->
                updateTokenSet(token) { it.copy(fontWeight = fontWeight) }
            },
        )
        val fontStyleControl = enumControl(
            name = "Font style",
            values = { FontStyle.entries },
            selectedValue = { state.tokenSet(token).fontStyle },
            onValueChange = { fontStyle ->
                updateTokenSet(token) { it.copy(fontStyle = fontStyle) }
            },
        )
        val fontSizeControl = Control.Slider(
            name = "Font size",
            value = { state.tokenSet(token).fontSize.value },
            onValueChange = { size ->
                updateTokenSet(token) { it.copy(fontSize = size.sp) }
            },
            valueRange = { 8f..96f },
            stepIncrement = 1f,
        )
        val lineHeightControl = Control.Slider(
            name = "Line height",
            value = { state.tokenSet(token).lineHeight.value },
            onValueChange = { lineHeight ->
                updateTokenSet(token) { it.copy(lineHeight = lineHeight.sp) }
            },
            valueRange = { 8f..96f },
            stepIncrement = 1f,
        )
        val letterSpacingControl = Control.Slider(
            name = "Letter spacing",
            value = { state.tokenSet(token).letterSpacing.value },
            onValueChange = { letterSpacing ->
                updateTokenSet(token) { it.copy(letterSpacing = letterSpacing.sp) }
            },
            valueRange = { -1f..2f },
            stepIncrement = 0.1f,
        )
        return Control.ControlColumn(
            name = token.name,
            controls = {
                persistentListOf(
                    fontFamilyControl,
                    fontWeightControl,
                    fontStyleControl,
                    fontSizeControl,
                    lineHeightControl,
                    letterSpacingControl,
                )
            },
            indent = true,
            expandedInitial = false,
        )
    }

    private fun updateTokenSet(
        token: TypographyToken,
        transform: (TypographyTokenSet) -> TypographyTokenSet,
    ) {
        themeController.updateSemantic {
            val current = it.typography.tokens.getValue(token)
            it.copy(typography = it.typography.withTokenSet(token, transform(current)))
        }
    }
}
