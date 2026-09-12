package com.embarrasdf.palette.modifiers

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.waitAndFindObject

class ModifiersPage(
    private val device: UiDevice,
) {
    fun assertIsDisplayed() {
        device.waitAndFindObject(By.text("ColorInvert"))
    }

    fun navigateToColorInvert() {
        navigateToModifier("ColorInvert")
    }

    fun navigateToColorSplit() {
        navigateToModifier("ColorSplit")
    }

    fun navigateToFade() {
        navigateToModifier("Fade")
    }

    fun navigateToNoise() {
        navigateToModifier("Noise")
    }

    fun navigateToPixelate() {
        navigateToModifier("Pixelate")
    }

    fun navigateToWarp() {
        navigateToModifier("Warp")
    }

    private fun navigateToModifier(modifierName: String) {
        device.waitAndFindObject(By.text(modifierName)).click()
    }
}
