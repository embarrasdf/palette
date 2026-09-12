package com.embarrasdf.palette.components.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

data class ProgressIndicatorStyle(
    val textStyle: TextStyle = TextStyle(),
)

@Composable
fun IndeterminateProgressIndicator(
    modifier: Modifier = Modifier,
    style: ProgressIndicatorStyle = ProgressIndicatorStyle(),
) {
    Text("...", style = style.textStyle, modifier = modifier)
}

@Preview
@Composable
fun PreviewIndeterminateProgressIndicator() {
    IndeterminateProgressIndicator()
}
