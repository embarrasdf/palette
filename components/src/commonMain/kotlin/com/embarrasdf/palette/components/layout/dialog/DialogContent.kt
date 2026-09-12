package com.embarrasdf.palette.components.layout.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.components.core.SurfaceStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.components.core.copy

data class DialogContentStyle(
    val titleStyle: TextStyle = TextStyle(),
    val messageStyle: TextStyle = TextStyle(),
    val surfaceStyle: SurfaceStyle = SurfaceStyle(),
    val buttonRowStyle: ConfirmCancelButtonRowStyle = ConfirmCancelButtonRowStyle(),
    val spacing: Dp = 16.dp,
    val padding: PaddingValues = PaddingValues(24.dp),
    val titlePadding: PaddingValues = PaddingValues(bottom = 16.dp),
    val messagePadding: PaddingValues = PaddingValues(bottom = 24.dp),
)

@Composable
fun DialogContent(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    style: DialogContentStyle = DialogContentStyle(),
    onConfirm: (() -> Unit)? = null,
) {
    DialogContent(
        title = title,
        message = message,
        buttonRow = { onDismissRequest, modifier ->
            onConfirm?.let {
                ConfirmCancelButtonRow(
                    onConfirm = it,
                    onDismiss = onDismissRequest,
                    style = style.buttonRowStyle,
                    modifier = modifier,
                )
            } ?: run {
                ConfirmButtonRow(
                    onConfirm = onDismissRequest,
                    style = style.buttonRowStyle.buttonStyle,
                    modifier = modifier,
                )
            }
        },
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        style = style,
    )
}

@Composable
fun ErrorDialogContent(
    title: String = "Error",
    message: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    style: DialogContentStyle = DialogContentStyle(),
) {
    DialogContent(
        title = title,
        message = message,
        buttonRow = { onDismissRequest, modifier ->
            ConfirmButtonRow(
                onConfirm = onDismissRequest,
                style = style.buttonRowStyle.buttonStyle,
                modifier = modifier,
            )
        },
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        style = style,
    )
}

@Composable
fun DialogContent(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    buttonRow: @Composable (onDismissRequest: () -> Unit, modifier: Modifier) -> Unit,
    modifier: Modifier = Modifier,
    style: DialogContentStyle = DialogContentStyle(),
) {
    DialogContent(
        title = title,
        modifier = modifier,
        style = style,
    ) {
        Text(
            text = message,
            style = style.messageStyle,
            modifier = Modifier
                .padding(style.messagePadding)
        )
        buttonRow(onDismissRequest, Modifier.align(Alignment.End))
    }
}

@Composable
fun DialogContent(
    title: String,
    modifier: Modifier = Modifier,
    style: DialogContentStyle = DialogContentStyle(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        style = style.surfaceStyle,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(style.spacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(style.padding)
        ) {
            Text(
                text = title,
                style = style.titleStyle,
                modifier = Modifier
                    .padding(style.titlePadding)
            )
            content()
        }
    }
}

@Preview
@Composable
private fun DialogContentPreview() {
    DialogContent(
        title = "Title",
        message = "Long message to show in the dialog content area.",
        onDismissRequest = {},
        onConfirm = {},
        modifier = Modifier
            .padding(16.dp)
    )
}

@Preview
@Composable
private fun ErrorDialogContentPreview() {
    ErrorDialogContent(
        message = "An error occurred while processing your request.",
        onDismissRequest = {},
        modifier = Modifier
            .padding(16.dp)
    )
}
