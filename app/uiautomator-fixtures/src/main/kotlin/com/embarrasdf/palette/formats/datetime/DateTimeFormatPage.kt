package com.embarrasdf.palette.formats.datetime

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import com.embarrasdf.palette.waitAndFindObject

class DateTimeFormatPage(
    private val device: UiDevice,
) {
    val demo: UiObject2
        get() = device.waitAndFindObject(By.descContains(""))
}
