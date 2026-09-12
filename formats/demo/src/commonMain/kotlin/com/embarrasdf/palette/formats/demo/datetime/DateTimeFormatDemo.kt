@file:OptIn(ExperimentalTime::class)

package com.embarrasdf.palette.formats.demo.datetime

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.DemoScope
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.theme.PaletteTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.enums.EnumEntries
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun <T : Enum<T>> DateTimeFormatDemo(
    initial: T,
    entries: EnumEntries<T>,
    format: (Instant, LocalDateTime, T) -> String,
    state: DateTimeFormatDemoState<T> = rememberDateTimeFormatDemoState(
        formatInitial = initial,
    ),
    control: DateTimeFormatDemoControl<T> = rememberDateTimeFormatDemoControl(
        entries = entries,
        state = state,
    ),
    modifier: Modifier = Modifier,
) {
    Demo(
        controls = control.controls,
        modifier = modifier
            .fillMaxSize()
    ) {
        DateTimeFormatDemo(
            state = state,
            format = format,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
fun <T> DemoScope.DateTimeFormatDemo(
    state: DateTimeFormatDemoState<T>,
    format: (Instant, LocalDateTime, T) -> String,
    modifier: Modifier = Modifier,
) {
    val formatted by state.time
        .map { format(it.first, it.second, state.token) }
        .collectAsStateWithLifecycle("")
    Text(
        text = formatted,
        style = PaletteTheme.component.core.text.headline,
        modifier = modifier,
    )
}

@Composable
fun <T> rememberDateTimeFormatDemoState(
    formatInitial: T,
    timeZoneInitial: TimeZone = TimeZone.currentSystemDefault(),
): DateTimeFormatDemoState<T> = rememberSaveable(
    formatInitial,
    timeZoneInitial,
    saver = DateTimeFormatDemoStateSaver(),
) {
    DateTimeFormatDemoState(
        timeZoneInitial = timeZoneInitial,
        tokenInitial = formatInitial,
    )
}

@Stable
class DateTimeFormatDemoState<T>(
    tokenInitial: T,
    timeZoneInitial: TimeZone = TimeZone.currentSystemDefault(),
) {
    val availableTimeZoneIds = setOf(TimeZone.currentSystemDefault().id, "UTC", "EST", "PST8PDT").toPersistentList()

    var timeZone by mutableStateOf(timeZoneInitial)
        internal set

    var token by mutableStateOf(tokenInitial)
        internal set

    val time: Flow<Pair<Instant, LocalDateTime>> = flow {
        while (true) {
            val now = Clock.System.now()
            emit(now to now.toLocalDateTime(timeZone))
            delay(1.seconds)
        }
    }
}

private const val timeZoneKey = "timeZone"
private const val tokenKey = "token"

fun <T> DateTimeFormatDemoStateSaver() = mapSaverSafe(
    save = { value ->
        mapOf(
            timeZoneKey to value.timeZone.id,
            tokenKey to value.token,
        )
    },
    restore = { map ->
        DateTimeFormatDemoState(
            timeZoneInitial = TimeZone.of(map[timeZoneKey] as String),
            tokenInitial = map[tokenKey] as T,
        )
    },
)

@Composable
fun <T : Enum<T>> rememberDateTimeFormatDemoControl(
    entries: EnumEntries<T>,
    state: DateTimeFormatDemoState<T>,
    onValueChange: (T) -> Unit = { state.token = it },
): DateTimeFormatDemoControl<T> = remember(state) {
    DateTimeFormatDemoControl(
        entries = entries,
        state = state,
        onValueChange = onValueChange,
    )
}

@Stable
class DateTimeFormatDemoControl<T : Enum<T>>(
    val entries: EnumEntries<T>,
    val state: DateTimeFormatDemoState<T>,
    val onValueChange: (T) -> Unit,
) {
    val controls = persistentListOf<Control>(
        enumControl(
            name = "Format",
            values = { entries },
            selectedValue = { state.token },
            onValueChange = onValueChange,
        ),
        Control.Dropdown(
            name = "Time zone",
            values = { state.availableTimeZoneIds.map { Control.Dropdown.DropdownItem(it, it) }.toPersistentList() },
            selectedIndex = { state.availableTimeZoneIds.indexOf(state.timeZone.id) },
            onValueChange = { index ->
                state.timeZone = TimeZone.of(state.availableTimeZoneIds[index])
            },
        ),
    )
}
