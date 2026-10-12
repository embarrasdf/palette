package com.embarrasdf.palette.modifiers

import android.graphics.Point
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.waitAndFindObject

private const val FirstDragOffsetPx = 100
private const val SecondDragOffsetPx = 200

class ColorInvertPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.desc("Amount"))
    }

    fun adjustColorInvert() {
        // One adjustment doesn't generate benchmark/profile frame data
        val amount = device.waitAndFindObject(By.desc("Amount"))
        amount.drag(Point(amount.visibleCenter.x + FirstDragOffsetPx, amount.visibleCenter.y))
        amount.drag(Point(amount.visibleCenter.x + SecondDragOffsetPx, amount.visibleCenter.y))
    }
}
