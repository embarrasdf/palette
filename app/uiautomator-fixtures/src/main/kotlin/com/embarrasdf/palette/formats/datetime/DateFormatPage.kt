package com.embarrasdf.palette.formats.datetime

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import com.embarrasdf.palette.waitAndFindObject

class DateFormatPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.text("Date"))
    }

    val demo: UiObject2
        get() = device.waitAndFindObject(By.descContains(""))
}
