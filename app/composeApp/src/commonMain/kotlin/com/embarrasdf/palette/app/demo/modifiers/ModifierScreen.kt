package com.embarrasdf.palette.app.demo.modifiers

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.app.preview.PalettePreview
import com.embarrasdf.palette.theme.components.layout.Scaffold
import com.embarrasdf.palette.modifiers.demo.ColorInvertDemo
import com.embarrasdf.palette.modifiers.demo.ColorSplitDemo
import com.embarrasdf.palette.modifiers.demo.FadeDemo
import com.embarrasdf.palette.modifiers.demo.NoiseDemo
import com.embarrasdf.palette.modifiers.demo.PixelateDemo
import com.embarrasdf.palette.modifiers.demo.WarpDemo
import com.embarrasdf.palette.theme.PaletteTheme

@Composable
fun ModifierScreen(
    modifierType: DemoModifier,
    onNavigateUp: () -> Unit,
    onThemeClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            DemoTopBar(
                title = modifierType.name,
                onNavigateUp = onNavigateUp,
                onThemeClick = onThemeClick,
            )
        },
    ) { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when (modifierType) {
            DemoModifier.ColorInvert -> ColorInvertDemo(
                modifier = modifier,
            )
            DemoModifier.ColorSplit -> ColorSplitDemo(
                modifier = modifier,
            )
            DemoModifier.Fade -> FadeDemo(
                modifier = modifier,
            )
            DemoModifier.Noise -> NoiseDemo(
                modifier = modifier,
            )
            DemoModifier.Pixelate -> PixelateDemo(
                modifier = modifier,
            )
            DemoModifier.Warp -> WarpDemo(
                modifier = modifier,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PalettePreview {
        ModifierScreen(
            modifierType = DemoModifier.ColorSplit,
            onNavigateUp = {},
            onThemeClick = {},
        )
    }
}
