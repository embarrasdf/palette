package com.embarrasdf.palette.formats.core

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.waitAndFindObject

class CoreFormatsPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.text("Number"))
    }

    fun navigateToNumber() {
        device.waitAndFindObject(By.text("Number")).click()
    }

    fun navigateToText() {
        device.waitAndFindObject(By.text("Text")).click()
    }
}
