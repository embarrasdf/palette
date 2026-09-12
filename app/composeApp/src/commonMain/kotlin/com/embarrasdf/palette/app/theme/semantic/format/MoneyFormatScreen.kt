package com.embarrasdf.palette.app.theme.semantic.format

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.formats.demo.money.MoneyFormatDemo
import com.embarrasdf.palette.formats.demo.money.MoneyFormatDemoControl
import com.embarrasdf.palette.formats.demo.money.MoneyFormatDemoState
import com.embarrasdf.palette.theme.components.demo.DemoList
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.theme.components.layout.BoxWithLabel
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.formats.money.MoneyFormat
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.semantic.format.Formats
import com.embarrasdf.palette.theme.semantic.format.money.MoneyFormatScheme
import com.embarrasdf.palette.theme.semantic.format.money.MoneyFormatToken
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun MoneyFormatScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberMoneyFormatScreenState(formats = themeController.semantic.formats)
    val control = rememberMoneyFormatScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Money format",
                onNavigateUp = onNavigateUp,
                onThemeClick = {},
                actions = {},
            )
        },
    ) { paddingValues ->
        DemoList(
            items = state.moneyFormatsByToken.entries.toList(),
            controls = control.controls,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { (token, _) ->
            BoxWithLabel(
                label = token.name,
                modifier = Modifier
                    .padding(horizontal = PaletteTheme.semantic.dimension.spacing.medium)
            ) {
                MoneyFormatDemo(
                    state = state.moneyFormatDemoStatesByToken[token]!!,
                )
            }
        }
    }
}

@Composable
fun rememberMoneyFormatScreenState(
    formats: Formats,
): MoneyFormatScreenState {
    return rememberSaveable(
        formats,
        saver = MoneyFormatScreenStateSaver(formats),
    ) {
        MoneyFormatScreenState(
            formats = formats,
        )
    }
}

@Stable
class MoneyFormatScreenState(
    val formats: Formats,
) {
    val moneyFormatScheme: MoneyFormatScheme
        get() = formats.moneyFormats

    val moneyFormatsByToken = MoneyFormatToken.entries.associateWith { token ->
        when (token) {
            MoneyFormatToken.Default -> moneyFormatScheme.default
        }
    }

    val moneyFormatDemoStatesByToken = MoneyFormatToken.entries.associateWith { token ->
        MoneyFormatDemoState(
            moneyFormatInitial = moneyFormatsByToken[token]!!,
        )
    }
}

fun MoneyFormatScreenStateSaver(
    formats: Formats,
) = mapSaverSafe(
    save = { state ->
        mapOf()
    },
    restore = { map ->
        MoneyFormatScreenState(
            formats = formats,
        )
    }
)

@Composable
fun rememberMoneyFormatScreenControl(
    state: MoneyFormatScreenState,
    themeController: ThemeController,
): MoneyFormatScreenControl {
    val formatControls = MoneyFormatToken.entries.map { token ->
        makeControlForToken(
            token = token,
            state = state,
            themeController = themeController,
        )
    }
    return remember(state, themeController, formatControls) {
        MoneyFormatScreenControl(
            state = state,
            themeController = themeController,
            formatControls = formatControls.toPersistentList(),
        )
    }
}

@Stable
class MoneyFormatScreenControl(
    val state: MoneyFormatScreenState,
    val themeController: ThemeController,
    val formatControls: PersistentList<Control>,
) {
    val controls: PersistentList<Control> = formatControls
}

@Composable
private fun makeControlForToken(
    token: MoneyFormatToken,
    state: MoneyFormatScreenState,
    themeController: ThemeController,
): Control {

    val moneyFormatDemoControl = MoneyFormatDemoControl(
        state = state.moneyFormatDemoStatesByToken[token]!!,
        onValueChange = { newValue ->
            themeController.updateSemantic {
                it.copy(
                    formats = state.formats.copy(
                        moneyFormats = state.moneyFormatScheme.update(
                            token = token,
                            moneyFormat = newValue,
                        )
                    )
                )
            }
        }
    )

    return Control.ControlColumn(
        name = token.name,
        controls = { moneyFormatDemoControl.controls },
        expandedInitial = false,
        indent = true,
    )
}

fun MoneyFormatScheme.update(
    token: MoneyFormatToken,
    moneyFormat: MoneyFormat,
): MoneyFormatScheme {
    return when (token) {
        MoneyFormatToken.Default -> this.copy(
            default = moneyFormat,
        )
    }
}
