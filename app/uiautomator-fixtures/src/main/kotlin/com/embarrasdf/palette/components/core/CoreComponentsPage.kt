package com.embarrasdf.palette.components.core

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.waitAndFindObject

class CoreComponentsPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.text("Button"))
    }

    fun navigateToButton() {
        device.waitAndFindObject(By.text("Button")).click()
    }

    fun navigateToText() {
        device.waitAndFindObject(By.text("Text")).click()
    }

    fun navigateToTextField() {
        device.waitAndFindObject(By.text("TextField")).click()
    }
}
