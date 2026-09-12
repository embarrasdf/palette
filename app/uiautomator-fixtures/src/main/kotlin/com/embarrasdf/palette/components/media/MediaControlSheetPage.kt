package com.embarrasdf.palette.components.media

import android.graphics.Point
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import com.embarrasdf.palette.waitAndFindObject

class MediaControlSheetPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.descContains("Media control bar"))
    }

    val mediaControlBar: UiObject2
        get() = device.waitAndFindObject(By.descContains("Media control bar"))

    fun expandSheet() {
        // Simple click did not generate enough frame timing metrics
        val bar = mediaControlBar
        val center = bar.visibleCenter
        val targetY = device.displayHeight / 4
        bar.drag(Point(center.x, targetY))
    }
}
