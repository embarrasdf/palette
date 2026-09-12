package com.embarrasdf.palette

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice

class MainCatalogPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.text("Components"))
    }

    fun navigateToComponents() {
        device.waitAndFindObject(By.text("Components")).click()
    }

    fun navigateToFormats() {
        device.waitAndFindObject(By.text("Formats")).click()
    }

    fun navigateToModifiers() {
        device.waitAndFindObject(By.text("Modifiers")).click()
    }
}
