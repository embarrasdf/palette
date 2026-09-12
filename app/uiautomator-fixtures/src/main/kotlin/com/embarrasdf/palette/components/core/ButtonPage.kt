package com.embarrasdf.palette.components.core

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import com.embarrasdf.palette.waitAndFindObject

class ButtonPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.desc("Demo Button"))
    }

    val button: UiObject2
        get() = device.waitAndFindObject(By.desc("Demo Button"))
}
