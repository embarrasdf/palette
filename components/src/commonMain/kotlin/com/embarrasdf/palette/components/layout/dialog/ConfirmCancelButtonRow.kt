package com.embarrasdf.palette.components.layout.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
data class ConfirmCancelButtonRowStyle(
    val buttonStyle: ConfirmButtonStyle = ConfirmButtonStyle(),
    val spacing: Dp = 16.dp,
)

@Composable
fun ConfirmCancelButtonRow(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    style: ConfirmCancelButtonRowStyle = ConfirmCancelButtonRowStyle(),
) {
    DialogContentButtonRow(
        modifier = modifier,
    ) {
        CancelButton(
            onDismiss = onDismiss,
            style = style.buttonStyle,
        )
        ConfirmButton(
            onConfirm = onConfirm,
            style = style.buttonStyle,
            modifier = Modifier
                .padding(start = style.spacing)
        )
    }
}

@Preview
@Composable
fun ConfirmCancelButtonRowPreview() {
    ConfirmCancelButtonRow(
        onConfirm = {},
        onDismiss = {},
    )
}
