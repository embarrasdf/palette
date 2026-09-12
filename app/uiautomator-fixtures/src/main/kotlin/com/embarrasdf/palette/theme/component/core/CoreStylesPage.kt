package com.embarrasdf.palette.theme.component.core

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.waitAndFindObject

class CoreStylesPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.text("Border"))
    }
}
