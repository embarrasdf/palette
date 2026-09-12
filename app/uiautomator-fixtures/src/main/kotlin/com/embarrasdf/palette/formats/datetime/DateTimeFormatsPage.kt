package com.embarrasdf.palette.formats.datetime

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.waitAndFindObject

class DateTimeFormatsPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.text("DateTime"))
    }

    fun navigateToDate() {
        device.waitAndFindObject(By.text("Date")).click()
    }

    fun navigateToDateTime() {
        device.waitAndFindObject(By.text("DateTime")).click()
    }

    fun navigateToInstant() {
        device.waitAndFindObject(By.text("Instant")).click()
    }

    fun navigateToTime() {
        device.waitAndFindObject(By.text("Time")).click()
    }
}
