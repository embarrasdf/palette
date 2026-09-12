package com.embarrasdf.palette.components.demo.auth

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.embarrasdf.palette.components.auth.AuthButton
import com.embarrasdf.palette.components.auth.AuthState
import com.embarrasdf.palette.theme.components.demo.Demo
import com.embarrasdf.palette.components.demo.control.Control
import com.embarrasdf.palette.components.demo.control.enumControl
import com.embarrasdf.palette.components.demo.control.paddingValuesControls
import com.embarrasdf.palette.components.util.PaddingValuesSaver
import com.embarrasdf.palette.components.util.mapSaverSafe
import com.embarrasdf.palette.components.util.restore
import com.embarrasdf.palette.components.util.save
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.auth.AuthButtonStyleToken
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AuthButtonDemo(
    state: AuthButtonDemoState = rememberAuthButtonDemoState(),
    control: AuthButtonDemoControl = rememberAuthButtonDemoControl(state),
    modifier: Modifier = Modifier,
) {
    Demo(
        controls = control.controls,
        modifier = modifier
            .fillMaxSize()
    ) {
        AuthButtonDemo(
            state = state,
            control = control,
        )
    }
}

@Composable
fun BoxWithConstraintsScope.AuthButtonDemo(
    modifier: Modifier = Modifier,
    state: AuthButtonDemoState = rememberAuthButtonDemoState(),
    control: AuthButtonDemoControl = rememberAuthButtonDemoControl(state),
) {
    val baseStyle = PaletteTheme.component.auth.authButton[state.style]
    AuthButton(
        authState = state.authState,
        style = baseStyle.copy(
            buttonStyle = baseStyle.buttonStyle.copy(contentPadding = state.contentPadding),
        ),
        onLogInClick = {},
        onLogOutClick = {},
        modifier = modifier
            .align(Alignment.Center)
            .padding(PaletteTheme.semantic.dimension.spacing.medium)
    )
}

@Composable
fun rememberAuthButtonDemoState(
    authStateInitial: AuthState = AuthState.LoggedOut,
    styleInitial: AuthButtonStyleToken = AuthButtonStyleToken.Secondary,
    contentPaddingInitial: PaddingValues =
        PaletteTheme.component.auth.authButton[styleInitial].buttonStyle.contentPadding,
): AuthButtonDemoState = rememberSaveable(
    saver = AuthButtonDemoStateSaver,
) {
    AuthButtonDemoState(
        authStateInitial = authStateInitial,
        styleInitial = styleInitial,
        contentPaddingInitial = contentPaddingInitial,
    )
}

@Stable
class AuthButtonDemoState(
    authStateInitial: AuthState,
    styleInitial: AuthButtonStyleToken,
    contentPaddingInitial: PaddingValues = PaddingValues(),
) {
    var authState by mutableStateOf(authStateInitial)
        internal set

    var style by mutableStateOf(styleInitial)
        internal set

    var contentPadding by mutableStateOf(contentPaddingInitial)
        internal set
}

private const val authStateKey = "authState"
private const val styleKey = "style"
private const val contentPaddingKey = "contentPadding"

val AuthButtonDemoStateSaver = mapSaverSafe(
    save = { value ->
        mapOf(
            authStateKey to value.authState,
            styleKey to value.style,
            contentPaddingKey to save(value.contentPadding, PaddingValuesSaver, this),
        )
    },
    restore = { map ->
        AuthButtonDemoState(
            authStateInitial = map[authStateKey] as AuthState,
            styleInitial = map[styleKey] as AuthButtonStyleToken,
            contentPaddingInitial = restore(map[contentPaddingKey], PaddingValuesSaver)!!,
        )
    },
)

@Composable
fun rememberAuthButtonDemoControl(
    state: AuthButtonDemoState,
): AuthButtonDemoControl = remember(state) { AuthButtonDemoControl(state) }

@Stable
class AuthButtonDemoControl(
    val state: AuthButtonDemoState,
) {
    val authStateControl = enumControl(
        name = "Auth state",
        values = { AuthState.entries },
        selectedValue = { state.authState },
        onValueChange = { state.authState = it },
    )

    val styleTokenControl = enumControl(
        name = "Token",
        values = { AuthButtonStyleToken.entries },
        selectedValue = { state.style },
        onValueChange = { state.style = it },
    )

    val contentPaddingControl = paddingValuesControls(
        name = "Content padding",
        value = { state.contentPadding },
        onValueChange = { state.contentPadding = it },
    )

    val styleControls = Control.ControlColumn(
        name = "Style",
        indent = true,
        expandedInitial = true,
        controls = {
            persistentListOf(
                styleTokenControl,
                contentPaddingControl,
            )
        },
    )

    val controls = persistentListOf<Control>(
        authStateControl,
        styleControls,
    )
}
