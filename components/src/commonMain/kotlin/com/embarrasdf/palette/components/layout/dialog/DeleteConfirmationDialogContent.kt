package com.embarrasdf.palette.components.layout.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DeleteConfirmationDialogContent(
    contentTitle: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    style: DialogContentStyle = DialogContentStyle(),
) {
    DialogContent(
        title = "Delete \"$contentTitle\"?",
        message = "This action cannot be undone.",
        onConfirm = onConfirm,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        style = style,
    )
}

@Preview
@Composable
fun DeleteConfirmationDialogContentPreview() {
    DeleteConfirmationDialogContent(
        contentTitle = "Log entry",
        onConfirm = {},
        onDismissRequest = {},
    )
}
