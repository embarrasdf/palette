package com.embarrasdf.palette.app.theme.semantic.dimension

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.embarrasdf.palette.theme.semantic.dimension.Size
import com.embarrasdf.palette.theme.semantic.dimension.SizeToken
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.semantic.dimension.copy
import com.embarrasdf.palette.theme.semantic.dimension.toSize
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun SizeScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberSizeScreenState(themeState = themeController)
    val control = rememberSizeScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Size",
                onNavigateUp = onNavigateUp,
                onThemeClick = {},
                actions = {},
            )
        },
    ) { paddingValues ->
        DemoList(
            items = SizeToken.entries.toList(),
            controls = control.controls,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { size ->
            SizeDemo(
                size = size,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun SizeDemo(
    modifier: Modifier = Modifier,
    size: SizeToken,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            PaletteTheme.semantic.dimension.spacing.small,
            Alignment.CenterVertically,
        ),
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .size(size.toSize())
                .border(1.dp, PaletteTheme.semantic.color.primary)
        )
        Text(
            text = size.name,
            style = PaletteTheme.component.core.text.headline,
        )
    }
}

@Composable
fun rememberSizeScreenState(
    themeState: ThemeState,
): SizeScreenState {
    return rememberSaveable(
        themeState,
        saver = SizeScreenStateSaver(themeState),
    ) {
        SizeScreenState(
            themeState = themeState,
        )
    }
}

@Stable
class SizeScreenState(
    val themeState: ThemeState,
) {
    val size: Size
        get() = themeState.semantic.dimension.size
}

fun SizeScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        mapOf<String, Any>()
    },
    restore = { map ->
        SizeScreenState(
            themeState = themeState,
        )
    }
)

@Composable
fun rememberSizeScreenControl(
    state: SizeScreenState,
    themeController: ThemeController,
): SizeScreenControl {
    return remember(state, themeController) {
        SizeScreenControl(state = state, themeController = themeController)
    }
}

@Stable
class SizeScreenControl(
    val state: SizeScreenState,
    val themeController: ThemeController,
) {
    val controls: PersistentList<Control> = SizeToken.entries.map { token ->
        Control.Slider(
            name = token.name,
            value = { token.toSize(state.size).value },
            onValueChange = { value ->
                val size = state.size.copy(token = token, value = value.dp)
                themeController.updateSemantic {
                    it.copy(dimension = it.dimension.copy(size = size))
                }
            },
            valueRange = { 0f..96f },
            stepIncrement = 4f,
        )
    }.let { persistentListOf(*it.toTypedArray()) }
}
