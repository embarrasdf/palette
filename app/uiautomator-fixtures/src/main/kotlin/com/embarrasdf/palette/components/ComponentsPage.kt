package com.embarrasdf.palette.components

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.waitAndFindObject

class ComponentsPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.text("Auth"))
    }

    fun navigateToCoreComponents() {
        device.waitAndFindObject(By.text("Core")).click()
    }

    fun navigateToMediaComponents() {
        device.waitAndFindObject(By.text("Media")).click()
    }
}
