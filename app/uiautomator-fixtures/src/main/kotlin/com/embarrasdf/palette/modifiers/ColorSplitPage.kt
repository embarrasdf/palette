package com.embarrasdf.palette.modifiers

import android.graphics.Point
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.waitAndFindObject

private const val FirstDragOffsetPx = 100

class ColorSplitPage(
    private val device: UiDevice,
) {
    fun adjustColorSplit() {
        val xAmount = device.waitAndFindObject(By.desc("X Amount"))
        xAmount.drag(Point(xAmount.visibleCenter.x + FirstDragOffsetPx, xAmount.visibleCenter.y))
        val yAmount = device.waitAndFindObject(By.desc("Y Amount"))
        yAmount.drag(Point(yAmount.visibleCenter.x + FirstDragOffsetPx, yAmount.visibleCenter.y))
    }
}
