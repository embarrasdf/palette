package com.embarrasdf.palette

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice

class DemoPage(
    private val device: UiDevice,
    private val title: String,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.text(title))
    }
}
