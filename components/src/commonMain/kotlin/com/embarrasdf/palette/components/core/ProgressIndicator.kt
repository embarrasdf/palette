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
    Text(text = "...", style = style.textStyle, modifier = modifier)
}

@Preview
@Composable
private fun PreviewIndeterminateProgressIndicator() {
    IndeterminateProgressIndicator()
}
