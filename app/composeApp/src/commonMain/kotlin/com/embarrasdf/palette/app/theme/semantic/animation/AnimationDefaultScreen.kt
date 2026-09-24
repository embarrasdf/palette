package com.embarrasdf.palette.app.theme.semantic.animation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.components.demo.control.Control
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
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AnimationDefaultScreen(
    themeController: ThemeController,
    onNavigateUp: () -> Unit,
) {
    val state = rememberAnimationDefaultScreenState(themeState = themeController)
    val control = rememberAnimationDefaultScreenControl(state = state, themeController = themeController)

    Scaffold(
        topBar = {
            DemoTopBar(
                title = "Default",
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
                spec = state.specState.spec,
                modifier = Modifier.fillMaxSize(),
                holdMillis = state.demoSubjectState.holdMillis,
            )
        }
    }
}

@Composable
fun rememberAnimationDefaultScreenState(
    themeState: ThemeState,
): AnimationDefaultScreenState {
    return rememberSaveable(
        themeState,
        saver = AnimationDefaultScreenStateSaver(themeState),
    ) {
        AnimationDefaultScreenState(
            themeState = themeState,
            specState = FiniteAnimationSpecState.from(themeState.semantic.animation.finite.default),
            demoSubjectState = AnimationDemoSubjectState(),
        )
    }
}

@Stable
class AnimationDefaultScreenState(
    val themeState: ThemeState,
    val specState: FiniteAnimationSpecState,
    val demoSubjectState: AnimationDemoSubjectState,
)

private const val specKey = "spec"
private const val demoSubjectKey = "demoSubject"

fun AnimationDefaultScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        mapOf(
            specKey to save(state.specState, FiniteAnimationSpecStateSaver, this),
            demoSubjectKey to save(state.demoSubjectState, AnimationDemoSubjectStateSaver, this),
        )
    },
    restore = { map ->
        AnimationDefaultScreenState(
            themeState = themeState,
            specState = restore(map[specKey], FiniteAnimationSpecStateSaver)!!,
            demoSubjectState = restore(map[demoSubjectKey], AnimationDemoSubjectStateSaver)!!,
        )
    }
)

@Composable
fun rememberAnimationDefaultScreenControl(
    state: AnimationDefaultScreenState,
    themeController: ThemeController,
): AnimationDefaultScreenControl {
    return remember(state, themeController) {
        AnimationDefaultScreenControl(state = state, themeController = themeController)
    }
}

@Stable
class AnimationDefaultScreenControl(
    val state: AnimationDefaultScreenState,
    val themeController: ThemeController,
) {
    val specControl = FiniteAnimationSpecControl(
        state = state.specState,
        onChanged = {
            themeController.updateSemantic {
                it.copy(
                    animation = it.animation.copy(
                        finite = it.animation.finite.copy(default = state.specState.spec),
                    ),
                )
            }
        },
    )

    private val demoSubjectControl = AnimationDemoSubjectControl(state.demoSubjectState)

    private val demoControl = Control.ControlColumn(
        name = "Demo Control",
        expandedInitial = false,
        controls = { demoSubjectControl.controls },
    )

    val controls: PersistentList<Control>
        get() = persistentListOf(
            *specControl.controls.toTypedArray(),
            demoControl,
        )
}
