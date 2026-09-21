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
import com.embarrasdf.palette.theme.semantic.animation.AnimationToken
import com.embarrasdf.palette.theme.semantic.animation.copy
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
                title = "Animation",
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
                spec = state.specState.spec,
                modifier = Modifier.fillMaxSize(),
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
        AnimationScreenState(
            themeState = themeState,
            specState = AnimationSpecState.from(themeState.semantic.animation.transition),
            subjectInitial = AnimationDemoSubject.Ball,
        )
    }
}

@Stable
class AnimationScreenState(
    val themeState: ThemeState,
    val specState: AnimationSpecState,
    subjectInitial: AnimationDemoSubject,
) {
    var subject by mutableStateOf(subjectInitial)
        internal set
}

private const val specStateKey = "specState"
private const val subjectKey = "subject"

fun AnimationScreenStateSaver(themeState: ThemeState) = mapSaverSafe(
    save = { state ->
        mapOf(
            specStateKey to save(state.specState, AnimationSpecStateSaver, this),
            subjectKey to state.subject,
        )
    },
    restore = { map ->
        AnimationScreenState(
            themeState = themeState,
            specState = restore(map[specStateKey], AnimationSpecStateSaver)!!,
            subjectInitial = map[subjectKey] as AnimationDemoSubject,
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
    val specControl = AnimationSpecControl(
        state = state.specState,
        onChanged = {
            themeController.updateSemantic {
                it.copy(
                    animation = it.animation.copy(
                        token = AnimationToken.Transition,
                        spec = state.specState.spec,
                    )
                )
            }
        },
    )

    val subjectControl = enumControl(
        name = "Subject",
        values = { AnimationDemoSubject.entries },
        selectedValue = { state.subject },
        onValueChange = { state.subject = it },
    )

    val controls: PersistentList<Control>
        get() = persistentListOf(
            subjectControl,
            *specControl.controls.toTypedArray(),
        )
}
