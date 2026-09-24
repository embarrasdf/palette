package com.embarrasdf.palette.app.theme.semantic.motion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.demo.subject.TransitionControl
import com.embarrasdf.palette.components.demo.subject.TransitionState
import com.embarrasdf.palette.components.demo.subject.TransitionStateSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.theme.control.ThemeController
import com.embarrasdf.palette.theme.control.ThemeState
import com.embarrasdf.palette.theme.semantic.motion.MotionToken
import com.embarrasdf.palette.theme.semantic.motion.copy
import com.embarrasdf.palette.theme.semantic.motion.toEnter
import com.embarrasdf.palette.theme.semantic.motion.toExit
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MotionScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberMotionScreenState(themeState = themeController)
    val control = rememberMotionScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Motion",
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
            val animation = PaletteTheme.semantic.animation
            val easings = PaletteTheme.primitive.easing
            val transition = state.activeTransitionState.transition
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(PaletteTheme.semantic.color.surface),
            ) {
                AnimatedVisibility(
                    visible = state.visible,
                    enter = transition.toEnter(animation, easings, forward = true),
                    exit = transition.toExit(animation, easings, forward = false),
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(PaletteTheme.semantic.color.primary),
                    )
                }
            }
        }
    }
}

@Composable
fun rememberMotionScreenState(
    themeState: ThemeState,
): MotionScreenState {
    return rememberSaveable(
        themeState,
        saver = MotionScreenStateSaver(themeState),
    ) {
        val motion = themeState.semantic.motion
        MotionScreenState(
            themeState = themeState,
            enterTransitionState = TransitionState.from(motion.enter),
            exitTransitionState = TransitionState.from(motion.exit),
            predictiveExitTransitionState = TransitionState.from(motion.predictiveExit),
            tokenInitial = MotionToken.Enter,
            visibleInitial = true,
        )
    }
}

@Stable
class MotionScreenState(
    val themeState: ThemeState,
    val enterTransitionState: TransitionState,
    val exitTransitionState: TransitionState,
    val predictiveExitTransitionState: TransitionState,
    tokenInitial: MotionToken,
    visibleInitial: Boolean,
) {
    var token by mutableStateOf(tokenInitial)
        internal set
    var visible by mutableStateOf(visibleInitial)
        internal set

    fun transitionState(token: MotionToken): TransitionState = when (token) {
        MotionToken.Enter -> enterTransitionState
        MotionToken.Exit -> exitTransitionState
        MotionToken.PredictiveExit -> predictiveExitTransitionState
    }

    val activeTransitionState: TransitionState
        get() = transitionState(token)
}

private const val enterKey = "enter"
private const val exitKey = "exit"
private const val predictiveExitKey = "predictiveExit"
private const val tokenKey = "token"
private const val visibleKey = "visible"

fun MotionScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        mapOf(
            enterKey to save(state.enterTransitionState, TransitionStateSaver, this),
            exitKey to save(state.exitTransitionState, TransitionStateSaver, this),
            predictiveExitKey to save(state.predictiveExitTransitionState, TransitionStateSaver, this),
            tokenKey to state.token,
            visibleKey to state.visible,
        )
    },
    restore = { map ->
        MotionScreenState(
            themeState = themeState,
            enterTransitionState = restore(map[enterKey], TransitionStateSaver)!!,
            exitTransitionState = restore(map[exitKey], TransitionStateSaver)!!,
            predictiveExitTransitionState = restore(map[predictiveExitKey], TransitionStateSaver)!!,
            tokenInitial = map[tokenKey] as MotionToken,
            visibleInitial = map[visibleKey] as Boolean,
        )
    }
)

@Composable
fun rememberMotionScreenControl(
    state: MotionScreenState,
    themeController: ThemeController,
): MotionScreenControl {
    return remember(state, themeController) {
        MotionScreenControl(state = state, themeController = themeController)
    }
}

@Stable
class MotionScreenControl(
    val state: MotionScreenState,
    val themeController: ThemeController,
) {
    private fun transitionControl(token: MotionToken): TransitionControl {
        val transitionState = state.transitionState(token)
        return TransitionControl(
            state = transitionState,
            onChanged = {
                themeController.updateSemantic {
                    it.copy(
                        motion = it.motion.copy(
                            token = token,
                            transition = transitionState.transition,
                        ),
                    )
                }
            },
        )
    }

    private val transitionControls: Map<MotionToken, TransitionControl> =
        MotionToken.entries.associateWith { transitionControl(it) }

    val tokenControl = enumControl(
        name = "Token",
        values = { MotionToken.entries },
        selectedValue = { state.token },
        onValueChange = { state.token = it },
    )

    private val enterExitControl = Control.Toggle(
        name = "Enter/Exit",
        value = { state.visible },
        onValueChange = { state.visible = it },
    )

    private val demoControl = Control.ControlColumn(
        name = "Demo Control",
        expandedInitial = true,
        controls = { persistentListOf(enterExitControl) },
    )

    val controls: PersistentList<Control>
        get() = persistentListOf(
            tokenControl,
            *transitionControls.getValue(state.token).controls.toTypedArray(),
            demoControl,
        )
}
