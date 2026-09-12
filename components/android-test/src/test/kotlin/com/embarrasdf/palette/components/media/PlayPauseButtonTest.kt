package com.embarrasdf.palette.components.media

import com.embarrasdf.palette.testing.PaparazziTestRule
import com.embarrasdf.palette.theme.PaletteTheme
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class PlayPauseButtonTest {

    @get:Rule
    val paparazzi = PaparazziTestRule

    @Test
    fun playPauseButton(
        @TestParameter isPlaying: Boolean,
        @TestParameter isEnabled: Boolean,
    ) {
        paparazzi.snapshot {
            PaletteTheme {
                PlayPauseButton(
                    isPlaying = isPlaying,
                    isEnabled = isEnabled,
                    style = PaletteTheme.component.media.playPauseButton,
                    onClick = {}
                )
            }
        }
    }
}
