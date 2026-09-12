package com.embarrasdf.palette.testing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import org.junit.rules.TestRule

class PaparazziRule(private val paparazzi: Paparazzi) : TestRule by paparazzi {
    fun snapshot(name: String? = null, composable: @Composable () -> Unit) {
        paparazzi.snapshot(name) {
            CompositionLocalProvider(LocalInspectionMode provides true) {
                composable()
            }
        }
    }
}

val PaparazziTestRule = PaparazziRule(
    Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        renderingMode = SessionParams.RenderingMode.SHRINK,
        showSystemUi = false,
        maxPercentDifference = 1.0,
    )
)
