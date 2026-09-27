package com.embarrasdf.palette.app.theme.semantic.animation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.demo.subject.AnimationDemoSubject
import com.embarrasdf.palette.components.demo.subject.InfiniteAnimationDemoSubject
import com.embarrasdf.palette.components.demo.subject.InfiniteAnimationSpecControl
import com.embarrasdf.palette.components.demo.subject.InfiniteAnimationSpecState
import com.embarrasdf.palette.components.demo.subject.InfiniteAnimationSpecStateSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.semantic.animation.infinite.InfiniteAnimationToken
import com.embarrasdf.palette.theme.semantic.animation.infinite.copy
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun InfiniteAnimationScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberInfiniteAnimationScreenState(themeState = themeController)
    val control = rememberInfiniteAnimationScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Infinite",
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
            InfiniteAnimationDemoSubject(
                subject = state.subject,
                spec = state.activeSpecState.spec,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun rememberInfiniteAnimationScreenState(
    themeState: ThemeState,
): InfiniteAnimationScreenState {
    return rememberSaveable(
        themeState,
        saver = InfiniteAnimationScreenStateSaver(themeState),
    ) {
        val infinite = themeState.semantic.animation.infinite
        InfiniteAnimationScreenState(
            themeState = themeState,
            defaultState = InfiniteAnimationSpecState.from(infinite.default),
            fastState = InfiniteAnimationSpecState.from(infinite.fast),
            slowState = InfiniteAnimationSpecState.from(infinite.slow),
            tokenInitial = InfiniteAnimationToken.Default,
            subjectInitial = AnimationDemoSubject.Scale,
        )
    }
}

@Stable
class InfiniteAnimationScreenState(
    val themeState: ThemeState,
    val defaultState: InfiniteAnimationSpecState,
    val fastState: InfiniteAnimationSpecState,
    val slowState: InfiniteAnimationSpecState,
    tokenInitial: InfiniteAnimationToken,
    subjectInitial: AnimationDemoSubject,
) {
    var token by mutableStateOf(tokenInitial)
        internal set
    var subject by mutableStateOf(subjectInitial)
        internal set

    fun specState(token: InfiniteAnimationToken): InfiniteAnimationSpecState = when (token) {
        InfiniteAnimationToken.Default -> defaultState
        InfiniteAnimationToken.Fast -> fastState
        InfiniteAnimationToken.Slow -> slowState
    }

    val activeSpecState: InfiniteAnimationSpecState
        get() = specState(token)
}

private const val defaultKey = "default"
private const val fastKey = "fast"
private const val slowKey = "slow"
private const val tokenKey = "token"
private const val subjectKey = "subject"

fun InfiniteAnimationScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        mapOf(
            defaultKey to save(state.defaultState, InfiniteAnimationSpecStateSaver, this),
            fastKey to save(state.fastState, InfiniteAnimationSpecStateSaver, this),
            slowKey to save(state.slowState, InfiniteAnimationSpecStateSaver, this),
            tokenKey to state.token,
            subjectKey to state.subject,
        )
    },
    restore = { map ->
        InfiniteAnimationScreenState(
            themeState = themeState,
            defaultState = restore(map[defaultKey], InfiniteAnimationSpecStateSaver)!!,
            fastState = restore(map[fastKey], InfiniteAnimationSpecStateSaver)!!,
            slowState = restore(map[slowKey], InfiniteAnimationSpecStateSaver)!!,
            tokenInitial = map[tokenKey] as InfiniteAnimationToken,
            subjectInitial = map[subjectKey] as AnimationDemoSubject,
        )
    }
)

@Composable
fun rememberInfiniteAnimationScreenControl(
    state: InfiniteAnimationScreenState,
    themeController: ThemeController,
): InfiniteAnimationScreenControl {
    return remember(state, themeController) {
        InfiniteAnimationScreenControl(state = state, themeController = themeController)
    }
}

@Stable
class InfiniteAnimationScreenControl(
    val state: InfiniteAnimationScreenState,
    val themeController: ThemeController,
) {
    private fun specControl(token: InfiniteAnimationToken): InfiniteAnimationSpecControl {
        val specState = state.specState(token)
        return InfiniteAnimationSpecControl(
            state = specState,
            onChanged = {
                themeController.updateSemantic {
                    it.copy(
                        animation = it.animation.copy(
                            infinite = it.animation.infinite.copy(token = token, spec = specState.spec),
                        ),
                    )
                }
            },
        )
    }

    private val specControls: Map<InfiniteAnimationToken, InfiniteAnimationSpecControl> =
        InfiniteAnimationToken.entries.associateWith { specControl(it) }

    val tokenControl = enumControl(
        name = "Token",
        values = { InfiniteAnimationToken.entries },
        selectedValue = { state.token },
        onValueChange = { state.token = it },
    )

    private val subjectControl = enumControl(
        name = "Subject",
        values = { AnimationDemoSubject.entries },
        selectedValue = { state.subject },
        onValueChange = { state.subject = it },
    )

    private val demoControl = Control.ControlColumn(
        name = "Demo Control",
        expandedInitial = false,
        controls = { persistentListOf(subjectControl) },
    )

    val controls: PersistentList<Control>
        get() = persistentListOf(
            tokenControl,
            *specControls.getValue(state.token).controls.toTypedArray(),
            demoControl,
        )
}
