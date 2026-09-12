package com.embarrasdf.palette.formats.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.embarrasdf.palette.MainCatalogPage
import com.embarrasdf.palette.appPackageName
import com.embarrasdf.palette.formats.FormatsPage
import com.embarrasdf.palette.formats.core.CoreFormatsPage
import com.embarrasdf.palette.formats.core.NumberFormatPage
import com.embarrasdf.palette.formats.core.TextFormatPage
import com.embarrasdf.palette.formats.datetime.DateTimeFormatPage
import com.embarrasdf.palette.formats.datetime.DateTimeFormatsPage
import com.embarrasdf.palette.formats.money.MoneyFormatPage
import com.embarrasdf.palette.formats.money.MoneyFormatsPage
import com.embarrasdf.palette.formatsPackageName
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generateFormatsProfile() {
        rule.collect(
            packageName = appPackageName,
            filterPredicate = { packageFilterPredicate(formatsPackageName, it) },
        ) {
            pressHome()
            startActivityAndWait()

            MainCatalogPage(device).navigateToFormats()

            FormatsPage(device).navigateToCoreFormats()

            CoreFormatsPage(device).apply {
                navigateToNumber()
                NumberFormatPage(device)
                device.pressBack()

                navigateToText()
                TextFormatPage(device)
                device.pressBack()
            }
            device.pressBack()

            FormatsPage(device).navigateToDateTimeFormats()
            DateTimeFormatsPage(device).apply {
                navigateToDate()
                DateTimeFormatPage(device)
                device.pressBack()

                navigateToDateTime()
                DateTimeFormatPage(device)
                device.pressBack()

                navigateToInstant()
                DateTimeFormatPage(device)
                device.pressBack()

                navigateToTime()
                DateTimeFormatPage(device)
                device.pressBack()
            }
            device.pressBack()

            FormatsPage(device).navigateToMoneyFormats()
            MoneyFormatsPage(device).apply {
                navigateToMoneyFormat()
                MoneyFormatPage(device)
                device.pressBack()
            }
            device.pressBack()
        }
    }

    private fun packageFilterPredicate(packageName: String, rule: String): Boolean {
        // Only capture rules in the library's package, excluding test app code
        // Rules are prefixed by tag characters, followed by JVM method signature,
        // e.g. `HSPLcom/mylibrary/LibraryClass;-><init>()V`, where `L`
        // signifies the start of the package/class, and '/' is divider instead of '.'
        return rule.contains("^.*L${packageName.replace(".", "/")}".toRegex())
    }
}
