package com.embarrasdf.palette.app.demo.modifiers

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.embarrasdf.palette.app.demo.DemoTopBar
import com.embarrasdf.palette.app.preview.PalettePreview
import com.embarrasdf.palette.modifiers.demo.ColorInvertDemo
import com.embarrasdf.palette.modifiers.demo.ColorSplitDemo
import com.embarrasdf.palette.modifiers.demo.FadeDemo
import com.embarrasdf.palette.modifiers.demo.NoiseDemo
import com.embarrasdf.palette.modifiers.demo.PixelateDemo
import com.embarrasdf.palette.modifiers.demo.WarpDemo
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.components.layout.Scaffold

@Composable
fun ModifierScreen(
    modifierType: DemoModifier,
    onNavigateUp: () -> Unit,
    onThemeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            DemoTopBar(
                title = modifierType.name,
                onNavigateUp = onNavigateUp,
                onThemeClick = onThemeClick,
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        val demoModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        when (modifierType) {
            DemoModifier.ColorInvert -> ColorInvertDemo(
                modifier = demoModifier,
            )
            DemoModifier.ColorSplit -> ColorSplitDemo(
                modifier = demoModifier,
            )
            DemoModifier.Fade -> FadeDemo(
                modifier = demoModifier,
            )
            DemoModifier.Noise -> NoiseDemo(
                modifier = demoModifier,
            )
            DemoModifier.Pixelate -> PixelateDemo(
                modifier = demoModifier,
            )
            DemoModifier.Warp -> WarpDemo(
                modifier = demoModifier,
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
