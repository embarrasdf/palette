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
import com.embarrasdf.palette.components.demo.subject.AnimationSpecControl
import com.embarrasdf.palette.components.demo.subject.AnimationSpecState
import com.embarrasdf.palette.components.demo.subject.AnimationSpecStateSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.semantic.animation.transition.TransitionToken
import com.embarrasdf.palette.theme.semantic.animation.transition.copy
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TransitionScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberTransitionScreenState(themeState = themeController)
    val control = rememberTransitionScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Transition",
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
                subject = state.subject,
                spec = state.activeSpecState.spec,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun rememberTransitionScreenState(
    themeState: ThemeState,
): TransitionScreenState {
    return rememberSaveable(
        themeState,
        saver = TransitionScreenStateSaver(themeState),
    ) {
        val transition = themeState.semantic.animation.transition
        TransitionScreenState(
            themeState = themeState,
            enterState = AnimationSpecState.from(transition.enter),
            exitState = AnimationSpecState.from(transition.exit),
            predictiveExitState = AnimationSpecState.from(transition.predictiveExit),
            tokenInitial = TransitionToken.Enter,
            subjectInitial = AnimationDemoSubject.Ball,
        )
    }
}

@Stable
class TransitionScreenState(
    val themeState: ThemeState,
    val enterState: AnimationSpecState,
    val exitState: AnimationSpecState,
    val predictiveExitState: AnimationSpecState,
    tokenInitial: TransitionToken,
    subjectInitial: AnimationDemoSubject,
) {
    var token by mutableStateOf(tokenInitial)
        internal set
    var subject by mutableStateOf(subjectInitial)
        internal set

    fun specState(token: TransitionToken): AnimationSpecState = when (token) {
        TransitionToken.Enter -> enterState
        TransitionToken.Exit -> exitState
        TransitionToken.PredictiveExit -> predictiveExitState
    }

    val activeSpecState: AnimationSpecState
        get() = specState(token)
}

private const val enterKey = "enter"
private const val exitKey = "exit"
private const val predictiveExitKey = "predictiveExit"
private const val tokenKey = "token"
private const val subjectKey = "subject"

fun TransitionScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        mapOf(
            enterKey to save(state.enterState, AnimationSpecStateSaver, this),
            exitKey to save(state.exitState, AnimationSpecStateSaver, this),
            predictiveExitKey to save(state.predictiveExitState, AnimationSpecStateSaver, this),
            tokenKey to state.token,
            subjectKey to state.subject,
        )
    },
    restore = { map ->
        TransitionScreenState(
            themeState = themeState,
            enterState = restore(map[enterKey], AnimationSpecStateSaver)!!,
            exitState = restore(map[exitKey], AnimationSpecStateSaver)!!,
            predictiveExitState = restore(map[predictiveExitKey], AnimationSpecStateSaver)!!,
            tokenInitial = map[tokenKey] as TransitionToken,
            subjectInitial = map[subjectKey] as AnimationDemoSubject,
        )
    }
)

@Composable
fun rememberTransitionScreenControl(
    state: TransitionScreenState,
    themeController: ThemeController,
): TransitionScreenControl {
    return remember(state, themeController) {
        TransitionScreenControl(state = state, themeController = themeController)
    }
}

@Stable
class TransitionScreenControl(
    val state: TransitionScreenState,
    val themeController: ThemeController,
) {
    private fun specControl(token: TransitionToken): AnimationSpecControl {
        val specState = state.specState(token)
        return AnimationSpecControl(
            state = specState,
            onChanged = {
                themeController.updateSemantic {
                    it.copy(
                        animation = it.animation.copy(
                            transition = it.animation.transition.copy(
                                token = token,
                                spec = specState.spec,
                            ),
                        ),
                    )
                }
            },
        )
    }

    private val specControls: Map<TransitionToken, AnimationSpecControl> =
        TransitionToken.entries.associateWith { specControl(it) }

    val tokenControl = enumControl(
        name = "Token",
        values = { TransitionToken.entries },
        selectedValue = { state.token },
        onValueChange = { state.token = it },
    )

    val subjectControl = enumControl(
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
