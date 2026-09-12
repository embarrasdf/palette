package com.embarrasdf.palette.app.theme.semantic.format.datetime

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.formats.demo.datetime.DateTimeFormatDemo
import com.embarrasdf.palette.formats.demo.datetime.DateTimeFormatDemoState
import com.embarrasdf.palette.formats.demo.datetime.rememberDateTimeFormatDemoControl
import com.embarrasdf.palette.theme.components.demo.DemoList
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.theme.components.layout.BoxWithLabel
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.formats.datetime.InstantFormatValue
import com.embarrasdf.palette.formats.datetime.format
import com.embarrasdf.palette.formats.datetime.toFormat
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.semantic.format.Formats
import com.embarrasdf.palette.theme.semantic.format.datetime.InstantFormatScheme
import com.embarrasdf.palette.theme.semantic.format.datetime.InstantFormatToken
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun InstantFormatSchemeScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberInstantFormatSchemeScreenState(
        formats = themeController.semantic.formats,
    )
    val control = rememberInstantFormatSchemeScreenControl(
        state = state,
        themeController = themeController,
    )

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Instant",
                onNavigateUp = onNavigateUp,
                onThemeClick = {},
                actions = {},
            )
        },
    ) { paddingValues ->
        DemoList(
            items = state.instantFormatsByToken.entries.toList(),
            controls = control.controls,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { (token, format) ->
            BoxWithLabel(
                label = token.name,
                modifier = Modifier
                    .padding(horizontal = PaletteTheme.semantic.dimension.spacing.medium)
            ) {
                DateTimeFormatDemo(
                    state = state.dateTimeFormatDemoStateByToken[token]!!,
                    format = { instant, _, _ ->
                        instant.format(format.toFormat())
                    },
                )
            }
        }
    }
}

@Composable
fun rememberInstantFormatSchemeScreenState(
    formats: Formats,
): InstantFormatSchemeScreenState {
    return rememberSaveable(
        formats.dateTimeFormats,
        saver = InstantFormatSchemeScreenStateSaver(formats),
    ) {
        InstantFormatSchemeScreenState(
            formats = formats,
        )
    }
}

@Stable
class InstantFormatSchemeScreenState(
    val formats: Formats,
) {
    val instantFormatScheme = formats.dateTimeFormats.instantFormatScheme

    val instantFormatsByToken = InstantFormatToken.entries.associateWith {
        when (it) {
            InstantFormatToken.Default -> instantFormatScheme.default
            InstantFormatToken.Long -> instantFormatScheme.long
            InstantFormatToken.Short -> instantFormatScheme.short
        }
    }

    val dateTimeFormatDemoStateByToken = InstantFormatToken.entries.associateWith {
        DateTimeFormatDemoState(
            tokenInitial = instantFormatsByToken[it]!!,
        )
    }
}

fun InstantFormatSchemeScreenStateSaver(
    formats: Formats,
) = mapSaverSafe(
    save = { state ->
        mapOf()
    },
    restore = { map ->
        InstantFormatSchemeScreenState(
            formats = formats,
        )
    }
)

@Composable
fun rememberInstantFormatSchemeScreenControl(
    state: InstantFormatSchemeScreenState,
    themeController: ThemeController,
): InstantFormatSchemeScreenControl {
    val formatControlByToken = InstantFormatToken.entries.associateWith { token ->
        makeControlForToken(
            token = token,
            state = state,
            themeController = themeController,
        )
    }
    return remember(state, themeController) {
        InstantFormatSchemeScreenControl(
            state = state,
            themeController = themeController,
            formatControlByToken = formatControlByToken,
        )
    }
}

@Stable
class InstantFormatSchemeScreenControl(
    val state: InstantFormatSchemeScreenState,
    val themeController: ThemeController,
    formatControlByToken: Map<InstantFormatToken, Control>,
) {
    val controls: PersistentList<Control> = persistentListOf(
        *formatControlByToken.values.toTypedArray(),
    )
}

@Composable
private fun makeControlForToken(
    token: InstantFormatToken,
    state: InstantFormatSchemeScreenState,
    themeController: ThemeController,
): Control {
    val demoControl = rememberDateTimeFormatDemoControl(
        entries = InstantFormatValue.entries,
        state = state.dateTimeFormatDemoStateByToken[token]!!,
        onValueChange = { newValue ->
            themeController.updateSemantic {
                it.copy(
                    formats = state.formats.copy(
                        dateTimeFormats = state.formats.dateTimeFormats.copy(
                            instantFormatScheme = state.instantFormatScheme.update(
                                format = token,
                                value = newValue,
                            )
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
    )
}

fun InstantFormatScheme.update(
    format: InstantFormatToken,
    value: InstantFormatValue,
) = when (format) {
    InstantFormatToken.Default -> copy(
        default = value,
    )
    InstantFormatToken.Short -> copy(
        short = value,
    )
    InstantFormatToken.Long -> copy(
        long = value,
    )
}
