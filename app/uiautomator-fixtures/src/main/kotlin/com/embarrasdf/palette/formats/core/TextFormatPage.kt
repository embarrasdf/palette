package com.embarrasdf.palette.formats.core

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import com.embarrasdf.palette.waitAndFindObject

class TextFormatPage(
    private val device: UiDevice,
) {
    val text: UiObject2
        get() = device.waitAndFindObject(By.text(""))
}
