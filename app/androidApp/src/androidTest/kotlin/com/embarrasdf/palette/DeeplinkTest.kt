package com.embarrasdf.palette

import android.content.Intent
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.embarrasdf.palette.components.ComponentsPage
import com.embarrasdf.palette.components.auth.AuthComponentsPage
import com.embarrasdf.palette.components.color.ColorComponentsPage
import com.embarrasdf.palette.components.core.ButtonPage
import com.embarrasdf.palette.components.core.CoreComponentsPage
import com.embarrasdf.palette.components.geometry.GeometryComponentsPage
import com.embarrasdf.palette.components.media.MediaComponentsPage
import com.embarrasdf.palette.components.media.MediaControlSheetPage
import com.embarrasdf.palette.components.money.MoneyComponentsPage
import com.embarrasdf.palette.formats.FormatsPage
import com.embarrasdf.palette.formats.core.CoreFormatsPage
import com.embarrasdf.palette.formats.core.NumberFormatPage
import com.embarrasdf.palette.formats.datetime.DateFormatPage
import com.embarrasdf.palette.formats.datetime.DateTimeFormatsPage
import com.embarrasdf.palette.formats.money.MoneyFormatPage
import com.embarrasdf.palette.formats.money.MoneyFormatsPage
import com.embarrasdf.palette.modifiers.ColorInvertPage
import com.embarrasdf.palette.modifiers.ModifiersPage
import com.embarrasdf.palette.theme.ThemePage
import com.embarrasdf.palette.theme.component.ComponentPage
import com.embarrasdf.palette.theme.component.core.CoreStylesPage
import com.embarrasdf.palette.theme.primitive.PrimitivePage
import com.embarrasdf.palette.theme.semantic.SemanticPage
import com.embarrasdf.palette.theme.semantic.format.ThemeFormatsPage
import com.embarrasdf.palette.theme.semantic.format.datetime.ThemeDateTimeFormatsPage
import com.embarrasdf.palette.theme.semantic.interaction.InteractionPage
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class DeeplinkTest {

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Test
    fun noIntentUri() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.startActivity(
            context.packageManager.getLaunchIntentForPackage(context.packageName)!!
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        MainCatalogPage(device).assertIsDisplayed()
    }

    @Test
    fun emptyDeeplinkPath() {
        startDeeplink("")
        MainCatalogPage(device).assertIsDisplayed()
    }

    // Main

    @Test
    fun mainGraph() {
        startDeeplink("main")
        MainCatalogPage(device).assertIsDisplayed()
    }

    @Test
    fun mainCatalog() {
        startDeeplink("main/catalog")
        MainCatalogPage(device).assertIsDisplayed()
    }

    // Components

    @Test
    fun componentsCatalog() {
        startDeeplink("components/catalog")
        ComponentsPage(device).assertIsDisplayed()
    }

    @Test
    fun coreComponentsCatalog() {
        startDeeplink("components/core/catalog")
        CoreComponentsPage(device).assertIsDisplayed()
    }

    @Test
    fun coreButton() {
        startDeeplink("components/core/button")
        ButtonPage(device).assertIsDisplayed()
    }

    @Test
    fun authComponentsCatalog() {
        startDeeplink("components/auth/catalog")
        AuthComponentsPage(device).assertIsDisplayed()
    }

    @Test
    fun authButton() {
        startDeeplink("components/auth/button")
        DemoPage(device, title = "Button").assertIsDisplayed()
    }

    @Test
    fun colorComponentsCatalog() {
        startDeeplink("components/color/catalog")
        ColorComponentsPage(device).assertIsDisplayed()
    }

    @Test
    fun colorPicker() {
        startDeeplink("components/color/color-picker")
        DemoPage(device, title = "ColorPicker").assertIsDisplayed()
    }

    @Test
    fun moneyComponentsCatalog() {
        startDeeplink("components/money/catalog")
        MoneyComponentsPage(device).assertIsDisplayed()
    }

    @Test
    fun currencyAmountField() {
        startDeeplink("components/money/currency-amount-field")
        DemoPage(device, title = "CurrencyAmountField").assertIsDisplayed()
    }

    @Test
    fun geometryComponentsCatalog() {
        startDeeplink("components/geometry/catalog")
        GeometryComponentsPage(device).assertIsDisplayed()
    }

    @Test
    fun curveStitch() {
        startDeeplink("components/geometry/curve-stitch")
        DemoPage(device, title = "CurveStitch").assertIsDisplayed()
    }

    @Test
    fun mediaComponentsCatalog() {
        startDeeplink("components/media/catalog")
        MediaComponentsPage(device).assertIsDisplayed()
    }

    @Test
    fun mediaControlSheet() {
        startDeeplink("components/media/media-control-sheet")
        MediaControlSheetPage(device).assertIsDisplayed()
    }

    // Modifiers

    @Test
    fun modifiersCatalog() {
        startDeeplink("modifiers/catalog")
        ModifiersPage(device).assertIsDisplayed()
    }

    @Test
    fun colorInvertModifier() {
        startDeeplink("modifiers/color-invert")
        ColorInvertPage(device).assertIsDisplayed()
    }

    // Formats

    @Test
    fun formatsCatalog() {
        startDeeplink("formats/catalog")
        FormatsPage(device).assertIsDisplayed()
    }

    @Test
    fun coreFormatsCatalog() {
        startDeeplink("formats/core/catalog")
        CoreFormatsPage(device).assertIsDisplayed()
    }

    @Test
    fun coreFormatNumber() {
        startDeeplink("formats/core/number")
        NumberFormatPage(device).assertIsDisplayed()
    }

    @Test
    fun dateTimeFormatsCatalog() {
        startDeeplink("formats/datetime/catalog")
        DateTimeFormatsPage(device).assertIsDisplayed()
    }

    @Test
    fun dateTimeFormatDate() {
        startDeeplink("formats/datetime/date")
        DateFormatPage(device).assertIsDisplayed()
    }

    @Test
    fun moneyFormatsCatalog() {
        startDeeplink("formats/money/catalog")
        MoneyFormatsPage(device).assertIsDisplayed()
    }

    @Test
    fun moneyFormat() {
        startDeeplink("formats/money/money-format")
        MoneyFormatPage(device).assertIsDisplayed()
    }

    // Theme

    @Test
    fun themeCatalog() {
        startDeeplink("theme/catalog")
        ThemePage(device).assertIsDisplayed()
    }

    @Test
    fun themePrimitiveCatalog() {
        startDeeplink("theme/primitive/catalog")
        PrimitivePage(device).assertIsDisplayed()
    }

    @Test
    fun themePrimitiveTypography() {
        startDeeplink("theme/primitive/typography")
        DemoPage(device, title = "Typography").assertIsDisplayed()
    }

    @Test
    fun themePrimitiveShape() {
        startDeeplink("theme/primitive/shape")
        DemoPage(device, title = "Shape").assertIsDisplayed()
    }

    @Test
    fun themeSemanticCatalog() {
        startDeeplink("theme/semantic/catalog")
        SemanticPage(device).assertIsDisplayed()
    }

    @Test
    fun themeComponentCatalog() {
        startDeeplink("theme/component/catalog")
        ComponentPage(device).assertIsDisplayed()
    }

    @Test
    fun themeComponentCoreCatalog() {
        startDeeplink("theme/component/core/catalog")
        CoreStylesPage(device).assertIsDisplayed()
    }

    @Test
    fun themeSemanticInteractionCatalog() {
        startDeeplink("theme/semantic/interaction/catalog")
        InteractionPage(device).assertIsDisplayed()
    }

    @Test
    fun themeSemanticFormatsCatalog() {
        startDeeplink("theme/semantic/formats/catalog")
        ThemeFormatsPage(device).assertIsDisplayed()
    }

    @Test
    fun themeSemanticFormatsDateTimeCatalog() {
        startDeeplink("theme/semantic/formats/datetime/catalog")
        ThemeDateTimeFormatsPage(device).assertIsDisplayed()
    }

    @Test
    fun themeSemanticFormatsDateTimeDate() {
        startDeeplink("theme/semantic/formats/datetime/date")
        DemoPage(device, title = "Date").assertIsDisplayed()
    }

    private fun startDeeplink(path: String) {
        InstrumentationRegistry.getInstrumentation().targetContext.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("palette://app/$path"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun startIntent(intent: Intent) {
        InstrumentationRegistry.getInstrumentation().targetContext.startActivity(
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
