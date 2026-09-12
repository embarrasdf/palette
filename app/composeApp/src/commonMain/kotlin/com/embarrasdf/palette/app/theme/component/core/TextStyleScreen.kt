package com.embarrasdf.palette.app.theme.component.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.theme.components.demo.DemoList
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.theme.components.core.Surface
import com.embarrasdf.palette.theme.components.layout.BoxWithLabel
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.semantic.color.ColorToken
import com.embarrasdf.palette.theme.semantic.typography.TypographyToken
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.semantic.format.core.TextFormatToken
import com.embarrasdf.palette.theme.component.core.SurfaceStyleToken
import com.embarrasdf.palette.theme.component.core.TextStyleToken
import com.embarrasdf.palette.theme.component.core.TextStyleTokenSet
import com.embarrasdf.palette.theme.component.core.toTextStyle
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TextStyleScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberTextStyleScreenState(themeState = themeController)
    val control = rememberTextStyleScreenControl(state = state, themeController = themeController)

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
            items = TextStyleToken.entries,
            controls = control.controls,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { token ->
            val textStyle = state.tokenSet(token).toTextStyle()
            BoxWithLabel(
                label = token.name,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Surface(
                    style = PaletteTheme.component.core.surface[state.surfaceToken(token)],
                ) { shapePadding ->
                    Text(
                        text = state.text,
                        style = textStyle,
                        modifier = Modifier.padding(shapePadding),
                    )
                }
            }
        }
    }
}

@Composable
fun rememberTextStyleScreenState(
    themeState: ThemeState,
    demoTextFieldState: TextFieldState = TextFieldState("Sphinx of black quartz, judge my vow"),
): TextStyleScreenState {
    return rememberSaveable(
        themeState,
        demoTextFieldState,
        saver = TextStyleScreenStateSaver(themeState),
    ) {
        TextStyleScreenState(
            themeState = themeState,
            demoTextFieldState = demoTextFieldState,
        )
    }
}

@Stable
class TextStyleScreenState(
    val themeState: ThemeState,
    val demoTextFieldState: TextFieldState = TextFieldState("Sphinx of black quartz, judge my vow"),
    surfaceTokensInitial: Map<TextStyleToken, SurfaceStyleToken> = emptyMap(),
) {
    val text by derivedStateOf {
        demoTextFieldState.text.toString()
    }

    private val surfaceTokens = mutableStateMapOf<TextStyleToken, SurfaceStyleToken>().apply {
        putAll(surfaceTokensInitial)
    }

    fun tokenSet(token: TextStyleToken): TextStyleTokenSet =
        themeState.component.text.getValue(token)

    fun surfaceToken(token: TextStyleToken): SurfaceStyleToken =
        surfaceTokens[token] ?: SurfaceStyleToken.Default

    fun setSurfaceToken(token: TextStyleToken, value: SurfaceStyleToken) {
        surfaceTokens[token] = value
    }
}

private const val demoTextFieldStateKey = "demoTextFieldState"
private const val surfaceTokenKeyPrefix = "surfaceToken_"

fun TextStyleScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        val map = mutableMapOf<String, Any?>(
            demoTextFieldStateKey to save(state.demoTextFieldState, TextFieldState.Saver, this),
        )
        TextStyleToken.entries.forEach { token ->
            map[surfaceTokenKeyPrefix + token.name] = state.surfaceToken(token).name
        }
        map
    },
    restore = { map ->
        val surfaceTokens = TextStyleToken.entries.mapNotNull { token ->
            (map[surfaceTokenKeyPrefix + token.name] as? String)
                ?.let { token to SurfaceStyleToken.valueOf(it) }
        }.toMap()
        TextStyleScreenState(
            themeState = themeState,
            demoTextFieldState = restore(map[demoTextFieldStateKey], TextFieldState.Saver)!!,
            surfaceTokensInitial = surfaceTokens,
        )
    }
)

@Composable
fun rememberTextStyleScreenControl(
    state: TextStyleScreenState,
    themeController: ThemeController,
): TextStyleScreenControl {
    return remember(state, themeController) {
        TextStyleScreenControl(state = state, themeController = themeController)
    }
}

@Stable
class TextStyleScreenControl(
    val state: TextStyleScreenState,
    val themeController: ThemeController,
) {
    val textStyleControls = TextStyleToken.entries.map { token ->
        makeControlForToken(
            token = token,
            state = state,
            themeController = themeController,
        )
    }

    val demoTextControl = Control.TextField(
        name = "Demo text",
        textFieldState = state.demoTextFieldState,
        includeLabel = false,
    )

    val controls: PersistentList<Control> = persistentListOf(
        demoTextControl,
        *textStyleControls.toTypedArray(),
    )
}

private fun makeControlForToken(
    token: TextStyleToken,
    state: TextStyleScreenState,
    themeController: ThemeController,
): Control {
    fun setTokenSet(value: TextStyleTokenSet) {
        themeController.updateComponent { it.copy(text = it.text + (token to value)) }
    }

    val typographyTokenControl = enumControl(
        name = "Typography token",
        values = { TypographyToken.entries },
        selectedValue = { state.tokenSet(token).typographyToken },
        onValueChange = { newValue ->
            setTokenSet(state.tokenSet(token).copy(typographyToken = newValue))
        },
    )

    val textFormatTokenControl = enumControl(
        name = "Text format token",
        values = { TextFormatToken.entries },
        selectedValue = { state.tokenSet(token).textFormatToken },
        onValueChange = { newValue ->
            setTokenSet(state.tokenSet(token).copy(textFormatToken = newValue))
        },
    )

    val colorTokenControl = enumControl(
        name = "Color token",
        values = { ColorToken.entries },
        selectedValue = { state.tokenSet(token).color },
        onValueChange = { newValue ->
            setTokenSet(state.tokenSet(token).copy(color = newValue))
        },
    )

    val surfaceTokenControl = enumControl(
        name = "Surface token",
        values = { SurfaceStyleToken.entries },
        selectedValue = { state.surfaceToken(token) },
        onValueChange = { newValue ->
            state.setSurfaceToken(token, newValue)
        },
    )

    val surfaceControlColumn = Control.ControlColumn(
        name = "Surface",
        indent = true,
        controls = { persistentListOf(surfaceTokenControl) },
        expandedInitial = false,
    )

    return Control.ControlColumn(
        name = token.name,
        controls = {
            persistentListOf(
                typographyTokenControl,
                textFormatTokenControl,
                colorTokenControl,
                surfaceControlColumn,
            )
        },
    )
}
