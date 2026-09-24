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
import com.embarrasdf.palette.components.demo.subject.AnimationDemoSubjectControl
import com.embarrasdf.palette.components.demo.subject.AnimationDemoSubjectState
import com.embarrasdf.palette.components.demo.subject.AnimationDemoSubjectStateSaver
import com.embarrasdf.palette.components.demo.subject.FiniteAnimationSpecControl
import com.embarrasdf.palette.components.demo.subject.FiniteAnimationSpecState
import com.embarrasdf.palette.components.demo.subject.FiniteAnimationSpecStateSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.semantic.animation.AnimationToken
import com.embarrasdf.palette.theme.semantic.animation.finite.copy
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AnimationScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberAnimationScreenState(themeState = themeController)
    val control = rememberAnimationScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Finite",
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
            AnimationDemoSubject(
                subject = state.demoSubjectState.subject,
                spec = state.activeSpecState.spec,
                modifier = Modifier.fillMaxSize(),
                holdMillis = state.demoSubjectState.holdMillis,
            )
        }
    }
}

@Composable
fun rememberAnimationScreenState(
    themeState: ThemeState,
): AnimationScreenState {
    return rememberSaveable(
        themeState,
        saver = AnimationScreenStateSaver(themeState),
    ) {
        val finite = themeState.semantic.animation.finite
        AnimationScreenState(
            themeState = themeState,
            defaultState = FiniteAnimationSpecState.from(finite.default),
            fastState = FiniteAnimationSpecState.from(finite.fast),
            slowState = FiniteAnimationSpecState.from(finite.slow),
            tokenInitial = AnimationToken.Default,
            demoSubjectState = AnimationDemoSubjectState(),
        )
    }
}

@Stable
class AnimationScreenState(
    val themeState: ThemeState,
    val defaultState: FiniteAnimationSpecState,
    val fastState: FiniteAnimationSpecState,
    val slowState: FiniteAnimationSpecState,
    tokenInitial: AnimationToken,
    val demoSubjectState: AnimationDemoSubjectState,
) {
    var token by mutableStateOf(tokenInitial)
        internal set

    fun specState(token: AnimationToken): FiniteAnimationSpecState = when (token) {
        AnimationToken.Default -> defaultState
        AnimationToken.Fast -> fastState
        AnimationToken.Slow -> slowState
    }

    val activeSpecState: FiniteAnimationSpecState
        get() = specState(token)
}

private const val defaultKey = "default"
private const val fastKey = "fast"
private const val slowKey = "slow"
private const val tokenKey = "token"
private const val demoSubjectKey = "demoSubject"

fun AnimationScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        mapOf(
            defaultKey to save(state.defaultState, FiniteAnimationSpecStateSaver, this),
            fastKey to save(state.fastState, FiniteAnimationSpecStateSaver, this),
            slowKey to save(state.slowState, FiniteAnimationSpecStateSaver, this),
            tokenKey to state.token,
            demoSubjectKey to save(state.demoSubjectState, AnimationDemoSubjectStateSaver, this),
        )
    },
    restore = { map ->
        AnimationScreenState(
            themeState = themeState,
            defaultState = restore(map[defaultKey], FiniteAnimationSpecStateSaver)!!,
            fastState = restore(map[fastKey], FiniteAnimationSpecStateSaver)!!,
            slowState = restore(map[slowKey], FiniteAnimationSpecStateSaver)!!,
            tokenInitial = map[tokenKey] as AnimationToken,
            demoSubjectState = restore(map[demoSubjectKey], AnimationDemoSubjectStateSaver)!!,
        )
    }
)

@Composable
fun rememberAnimationScreenControl(
    state: AnimationScreenState,
    themeController: ThemeController,
): AnimationScreenControl {
    return remember(state, themeController) {
        AnimationScreenControl(state = state, themeController = themeController)
    }
}

@Stable
class AnimationScreenControl(
    val state: AnimationScreenState,
    val themeController: ThemeController,
) {
    private fun specControl(token: AnimationToken): FiniteAnimationSpecControl {
        val specState = state.specState(token)
        return FiniteAnimationSpecControl(
            state = specState,
            onChanged = {
                themeController.updateSemantic {
                    it.copy(
                        animation = it.animation.copy(
                            finite = it.animation.finite.copy(token = token, spec = specState.spec),
                        ),
                    )
                }
            },
        )
    }

    private val specControls: Map<AnimationToken, FiniteAnimationSpecControl> =
        AnimationToken.entries.associateWith { specControl(it) }

    val tokenControl = enumControl(
        name = "Token",
        values = { AnimationToken.entries },
        selectedValue = { state.token },
        onValueChange = { state.token = it },
    )

    private val demoSubjectControl = AnimationDemoSubjectControl(state.demoSubjectState)

    private val demoControl = Control.ControlColumn(
        name = "Demo Control",
        expandedInitial = false,
        controls = { demoSubjectControl.controls },
    )

    val controls: PersistentList<Control>
        get() = persistentListOf(
            tokenControl,
            *specControls.getValue(state.token).controls.toTypedArray(),
            demoControl,
        )
}
