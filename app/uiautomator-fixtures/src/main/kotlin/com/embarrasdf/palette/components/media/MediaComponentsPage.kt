package com.embarrasdf.palette.components.media

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.waitAndFindObject

class MediaComponentsPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.text("MediaControlSheet"))
    }

    fun navigateToMediaControlSheet() {
        device.waitAndFindObject(By.text("MediaControlSheet")).click()
    }
}
