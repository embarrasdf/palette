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
                subject = state.demoSubjectState.subject,
                spec = state.activeSpecState.spec,
                modifier = Modifier.fillMaxSize(),
                holdMillis = state.demoSubjectState.holdMillis,
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
            enterState = FiniteAnimationSpecState.from(transition.enter),
            exitState = FiniteAnimationSpecState.from(transition.exit),
            predictiveExitState = FiniteAnimationSpecState.from(transition.predictiveExit),
            tokenInitial = TransitionToken.Enter,
            demoSubjectState = AnimationDemoSubjectState(),
        )
    }
}

@Stable
class TransitionScreenState(
    val themeState: ThemeState,
    val enterState: FiniteAnimationSpecState,
    val exitState: FiniteAnimationSpecState,
    val predictiveExitState: FiniteAnimationSpecState,
    tokenInitial: TransitionToken,
    val demoSubjectState: AnimationDemoSubjectState,
) {
    var token by mutableStateOf(tokenInitial)
        internal set

    fun specState(token: TransitionToken): FiniteAnimationSpecState = when (token) {
        TransitionToken.Enter -> enterState
        TransitionToken.Exit -> exitState
        TransitionToken.PredictiveExit -> predictiveExitState
    }

    val activeSpecState: FiniteAnimationSpecState
        get() = specState(token)
}

private const val enterKey = "enter"
private const val exitKey = "exit"
private const val predictiveExitKey = "predictiveExit"
private const val tokenKey = "token"
private const val demoSubjectKey = "demoSubject"

fun TransitionScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        mapOf(
            enterKey to save(state.enterState, FiniteAnimationSpecStateSaver, this),
            exitKey to save(state.exitState, FiniteAnimationSpecStateSaver, this),
            predictiveExitKey to save(state.predictiveExitState, FiniteAnimationSpecStateSaver, this),
            tokenKey to state.token,
            demoSubjectKey to save(state.demoSubjectState, AnimationDemoSubjectStateSaver, this),
        )
    },
    restore = { map ->
        TransitionScreenState(
            themeState = themeState,
            enterState = restore(map[enterKey], FiniteAnimationSpecStateSaver)!!,
            exitState = restore(map[exitKey], FiniteAnimationSpecStateSaver)!!,
            predictiveExitState = restore(map[predictiveExitKey], FiniteAnimationSpecStateSaver)!!,
            tokenInitial = map[tokenKey] as TransitionToken,
            demoSubjectState = restore(map[demoSubjectKey], AnimationDemoSubjectStateSaver)!!,
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
    private fun specControl(token: TransitionToken): FiniteAnimationSpecControl {
        val specState = state.specState(token)
        return FiniteAnimationSpecControl(
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

    private val specControls: Map<TransitionToken, FiniteAnimationSpecControl> =
        TransitionToken.entries.associateWith { specControl(it) }

    val tokenControl = enumControl(
        name = "Token",
        values = { TransitionToken.entries },
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
