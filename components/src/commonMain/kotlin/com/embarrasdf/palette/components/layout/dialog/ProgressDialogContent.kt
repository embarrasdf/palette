package com.embarrasdf.palette.components.layout.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.components.core.IndeterminateProgressIndicator
import com.embarrasdf.palette.components.core.ProgressIndicatorStyle

data class ProgressDialogContentStyle(
    val dialogContentStyle: DialogContentStyle = DialogContentStyle(),
    val progressIndicatorStyle: ProgressIndicatorStyle = ProgressIndicatorStyle(),
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun IndeterminateProgressDialogContent(
    title: String = "",
    modifier: Modifier = Modifier,
    style: ProgressDialogContentStyle = ProgressDialogContentStyle(),
) {
    DialogContent(
        title = title,
        modifier = modifier,
        style = style.dialogContentStyle,
    ) {
        IndeterminateProgressIndicator(
            style = style.progressIndicatorStyle,
            modifier = Modifier,
        )
    }
}

@Preview
@Composable
private fun IndeterminateProgressDialogContentPreview() {
    IndeterminateProgressDialogContent(
        title = "Doing something"
    )
}
