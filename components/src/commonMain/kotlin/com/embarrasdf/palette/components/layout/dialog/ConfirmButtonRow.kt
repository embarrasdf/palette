package com.embarrasdf.palette.components.layout.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ConfirmButtonRow(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    style: ConfirmButtonStyle = ConfirmButtonStyle(),
) {
    DialogContentSingleButtonRow(
        modifier = modifier,
    ) {
        ConfirmButton(
            onConfirm = onConfirm,
            style = style,
        )
    }
}

@Preview
@Composable
private fun ConfirmButtonRow() {
    ConfirmButtonRow(
        onConfirm = {},
    )
}
