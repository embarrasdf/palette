package com.embarrasdf.palette.modifiers

import android.graphics.Point
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.waitAndFindObject

class ColorInvertPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.desc("Amount"))
    }

    fun adjustColorInvert() {
        // One adjustment doesn't generate benchmark/profile frame data
        val amount = device.waitAndFindObject(By.desc("Amount"))
        amount.drag(Point(amount.visibleCenter.x + 100, amount.visibleCenter.y))
        amount.drag(Point(amount.visibleCenter.x + 200, amount.visibleCenter.y))
    }
}
