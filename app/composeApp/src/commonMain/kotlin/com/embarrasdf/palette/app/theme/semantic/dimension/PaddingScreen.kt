package com.embarrasdf.palette.app.theme.semantic.dimension

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.demo.DemoList
import com.embarrasdf.palette.theme.components.demo.control.spacingTokenPaddingControls
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.control.rememberThemeController
import com.embarrasdf.palette.theme.semantic.dimension.PaddingScheme
import com.embarrasdf.palette.theme.semantic.dimension.PaddingValuesToken
import com.embarrasdf.palette.theme.semantic.dimension.copy
import com.embarrasdf.palette.theme.semantic.dimension.toPaddingValues
import com.embarrasdf.palette.theme.semantic.dimension.tokenSet
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun PaddingScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberPaddingScreenState(themeState = themeController)
    val control = rememberPaddingScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Padding",
                onNavigateUp = onNavigateUp,
                onThemeClick = {},
                actions = {},
            )
        },
    ) { paddingValues ->
        DemoList(
            items = PaddingValuesToken.entries.toList(),
            controls = control.controls,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { token ->
            PaddingDemo(
                token = token,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun PaddingDemo(
    modifier: Modifier = Modifier,
    token: PaddingValuesToken,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .border(1.dp, PaletteTheme.semantic.color.primary)
            .padding(token.toPaddingValues())
            .border(1.dp, PaletteTheme.semantic.color.primary)
    ) {
        Text(
            text = token.name,
            style = PaletteTheme.component.core.text.headline,
        )
    }
}

@Composable
fun rememberPaddingScreenState(
    themeState: ThemeState,
): PaddingScreenState {
    return rememberSaveable(
        themeState,
        saver = PaddingScreenStateSaver(themeState),
    ) {
        PaddingScreenState(
            themeState = themeState,
        )
    }
}

@Stable
class PaddingScreenState(
    val themeState: ThemeState,
) {
    val paddingScheme: PaddingScheme
        get() = themeState.semantic.dimension.padding
}

fun PaddingScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        mapOf<String, Any>()
    },
    restore = { map ->
        PaddingScreenState(
            themeState = themeState,
        )
    }
)

@Composable
fun rememberPaddingScreenControl(
    state: PaddingScreenState,
    themeController: ThemeController,
): PaddingScreenControl {
    return remember(state, themeController) {
        PaddingScreenControl(state = state, themeController = themeController)
    }
}

@Stable
class PaddingScreenControl(
    val state: PaddingScreenState,
    val themeController: ThemeController,
) {
    val controls: PersistentList<Control> = PaddingValuesToken.entries.map { token ->
        spacingTokenPaddingControls(
            name = token.name,
            expandedInitial = true,
            value = { state.paddingScheme.tokenSet(token) },
            onValueChange = { tokenSet ->
                themeController.updateSemantic {
                    it.copy(
                        dimension = it.dimension.copy(
                            padding = it.dimension.padding.copy(
                                token = token,
                                tokenSet = tokenSet,
                            ),
                        ),
                    )
                }
            },
        )
    }.toPersistentList()
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        PaddingScreen(
            themeController = rememberThemeController(),
            onNavigateUp = {},
        )
    }
}
