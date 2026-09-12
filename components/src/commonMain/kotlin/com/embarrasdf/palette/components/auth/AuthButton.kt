package com.embarrasdf.palette.components.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle

enum class AuthState {
    Loading,
    LoggedOut,
    LoggedIn,
}

data class AuthButtonStyle(
    val buttonStyle: ButtonStyle = ButtonStyle(),
    val textStyle: TextStyle = TextStyle(),
)

@Composable
fun AuthButton(
    authState: AuthState,
    onLogInClick: () -> Unit,
    onLogOutClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: AuthButtonStyle = AuthButtonStyle(),
) {
    when (authState) {
        AuthState.Loading -> AuthButtonText(
            authState = authState,
            style = style,
            modifier = modifier,
        )
        AuthState.LoggedOut -> LogInButton(
            style = style,
            onLogInClick = onLogInClick,
            modifier = modifier,
        )
        AuthState.LoggedIn -> LogOutButton(
            style = style,
            onLogOutClick = onLogOutClick,
            modifier = modifier,
        )
    }
}

@Composable
fun LogInButton(
    onLogInClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: AuthButtonStyle = AuthButtonStyle(),
) {
    AuthButton(
        authState = AuthState.LoggedOut,
        style = style,
        onClick = onLogInClick,
        modifier = modifier,
    )
}

@Composable
fun LogOutButton(
    onLogOutClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: AuthButtonStyle = AuthButtonStyle(),
) {
    AuthButton(
        authState = AuthState.LoggedIn,
        style = style,
        onClick = onLogOutClick,
        modifier = modifier,
    )
}

@Composable
fun AuthButton(
    authState: AuthState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: AuthButtonStyle = AuthButtonStyle(),
) {
    Button(
        style = style.buttonStyle,
        onClick = onClick,
        modifier = modifier,
    ) {
        AuthButtonText(
            authState = authState,
            style = style,
        )
    }
}

@Composable
fun AuthButtonText(
    authState: AuthState,
    modifier: Modifier = Modifier,
    style: AuthButtonStyle = AuthButtonStyle(),
) {
    Text(
        text = when (authState) {
            AuthState.Loading -> "..."
            AuthState.LoggedIn -> "Log out"
            AuthState.LoggedOut -> "Log in"
        },
        style = style.textStyle,
        modifier = modifier,
    )
}

internal class AuthStatePreviewParameterProvider : PreviewParameterProvider<AuthState> {
    override val values = sequenceOf(AuthState.Loading, AuthState.LoggedOut, AuthState.LoggedIn)
}

@Preview
@Composable
fun PreviewAuthButton(
    @PreviewParameter(AuthStatePreviewParameterProvider::class) authState: AuthState,
) {
    Surface {
        AuthButton(
            authState = authState,
            onLogInClick = {},
            onLogOutClick = {},
        )
    }
}
