package com.embarrasdf.palette.components.layout.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DialogContentButtonRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun DialogContentSingleButtonRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        content()
    }
}

@Preview
@Composable
fun DialogContentButtonRowPreview() {
    DialogContentButtonRow {
        CancelButton(
            onDismiss = {},
        )
        ConfirmButton(
            onConfirm = {},
        )
    }
}

@Preview
@Composable
fun DialogContentSingleButtonRowPreview() {
    DialogContentSingleButtonRow {
        ConfirmButton(
            onConfirm = {},
        )
    }
}
