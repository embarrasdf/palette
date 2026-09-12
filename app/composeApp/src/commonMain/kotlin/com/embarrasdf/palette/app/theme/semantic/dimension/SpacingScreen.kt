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
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.theme.components.demo.DemoList
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.semantic.spacing.Spacing
import com.embarrasdf.palette.theme.semantic.spacing.SpacingToken
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.semantic.spacing.copy
import com.embarrasdf.palette.theme.semantic.spacing.toSpacing
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun SpacingScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberSpacingScreenState(themeState = themeController)
    val control = rememberSpacingScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Spacing",
                onNavigateUp = onNavigateUp,
                onThemeClick = {},
                actions = {},
            )
        },
    ) { paddingValues ->
        DemoList(
            items = state.spacingByToken.keys.toList(),
            controls = control.controls,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { spacing ->
            SpacingDemo(
                spacing = spacing,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun SpacingDemo(
    modifier: Modifier = Modifier,
    spacing: SpacingToken,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .border(1.dp, PaletteTheme.semantic.color.primary)
            .padding(spacing.toSpacing())
            .border(1.dp, PaletteTheme.semantic.color.primary)
    ) {
        Text(
            text = spacing.name,
            style = PaletteTheme.component.core.text.headline,
        )
    }
}

@Composable
fun rememberSpacingScreenState(
    themeState: ThemeState,
): SpacingScreenState {
    return rememberSaveable(
        themeState,
        saver = SpacingScreenStateSaver(themeState),
    ) {
        SpacingScreenState(
            themeState = themeState,
        )
    }
}

@Stable
class SpacingScreenState(
    val themeState: ThemeState,
) {
    val spacing: Spacing
        get() = themeState.semantic.dimension.spacing

    val spacingByToken get() = SpacingToken.entries.associateWith { token ->
        token.toSpacing(spacing)
    }
}

fun SpacingScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        mapOf()
    },
    restore = { map ->
        SpacingScreenState(
            themeState = themeState,
        )
    }
)

@Composable
fun rememberSpacingScreenControl(
    state: SpacingScreenState,
    themeController: ThemeController,
): SpacingScreenControl {
    return remember(state, themeController) {
        SpacingScreenControl(state = state, themeController = themeController)
    }
}

@Stable
class SpacingScreenControl(
    val state: SpacingScreenState,
    val themeController: ThemeController,
) {
    val spacingControls = SpacingToken.entries.map { token ->
        makeControlForToken(
            token = token,
            state = state,
            themeController = themeController,
        )
    }

    val controls: PersistentList<Control> = persistentListOf(
        *spacingControls.toTypedArray(),
    )
}

private fun makeControlForToken(
    token: SpacingToken,
    state: SpacingScreenState,
    themeController: ThemeController,
): Control {
    return Control.Slider(
        name = token.name,
        value = { state.spacingByToken[token]!!.value },
        onValueChange = { radius ->
            val spacing = state.spacing.copy(
                token = token,
                value = radius.dp,
            )
            themeController.updateSemantic {
                it.copy(dimension = it.dimension.copy(spacing = spacing))
            }
        },
        valueRange = { 0f..64f },
        stepIncrement = 4f,
    )
}
