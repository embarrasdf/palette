package com.embarrasdf.palette.modifiers

import android.graphics.Point
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.waitAndFindObject

private const val FirstDragOffsetPx = 100
private const val SecondDragOffsetPx = 200

class FadePage(
    private val device: UiDevice,
) {
    fun adjustFade() {
        // One adjustment doesn't generate benchmark/profile frame data
        val xAmount = device.waitAndFindObject(By.desc("Fade length"))
        xAmount.drag(Point(xAmount.visibleCenter.x + FirstDragOffsetPx, xAmount.visibleCenter.y))
        xAmount.drag(Point(xAmount.visibleCenter.x + SecondDragOffsetPx, xAmount.visibleCenter.y))
    }
}
