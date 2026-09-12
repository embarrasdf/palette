package com.embarrasdf.palette.modifiers

import android.graphics.Point
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.waitAndFindObject

class NoisePage(
    private val device: UiDevice,
) {
    fun adjustNoise() {
        // One adjustment doesn't generate benchmark/profile frame data
        val xAmount = device.waitAndFindObject(By.desc("Amount"))
        xAmount.drag(Point(xAmount.visibleCenter.x + 100, xAmount.visibleCenter.y))
        xAmount.drag(Point(xAmount.visibleCenter.x + 200, xAmount.visibleCenter.y))
    }
}
