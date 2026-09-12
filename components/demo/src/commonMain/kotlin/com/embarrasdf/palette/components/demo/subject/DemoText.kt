package com.embarrasdf.palette.components.demo.subject

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.components.core.TextStyle

@Composable
fun DemoText(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = PaletteTheme.component.core.text.display,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PaletteTheme.semantic.color.surface)
    ) {
        Text(
            text = "Hello world",
            style = textStyle,
            modifier = modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    DemoText()
}
