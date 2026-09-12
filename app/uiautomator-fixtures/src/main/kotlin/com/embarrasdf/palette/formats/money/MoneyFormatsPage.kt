package com.embarrasdf.palette.formats.money

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.waitAndFindObject

class MoneyFormatsPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.text("Money"))
    }

    fun navigateToMoneyFormat() {
        device.waitAndFindObject(By.text("MoneyFormat")).click()
    }
}
