package com.embarrasdf.palette.benchmark.components.media

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.embarrasdf.palette.MainCatalogPage
import com.embarrasdf.palette.appPackageName
import com.embarrasdf.palette.components.ComponentsPage
import com.embarrasdf.palette.components.media.MediaComponentsPage
import com.embarrasdf.palette.components.media.MediaControlSheetPage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaControlSheetBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun compilationModeNone() = mediaControlSheetOpen(CompilationMode.None())

    @Test
    fun compilationModePartial() =
        mediaControlSheetOpen(CompilationMode.Partial(BaselineProfileMode.Require))

    @OptIn(ExperimentalMetricApi::class)
    fun mediaControlSheetOpen(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = appPackageName,
        metrics = listOf(
            FrameTimingMetric(),
            TraceSectionMetric("MediaControlSheet", TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("MediaControlBar", TraceSectionMetric.Mode.Sum),
            TraceSectionMetric(
                "MediaControlBar:MediaItemArtwork:layout",
                TraceSectionMetric.Mode.Sum
            ),
        ),
        iterations = 5,
        startupMode = StartupMode.WARM,
        compilationMode = compilationMode,
        setupBlock = {
            pressHome()
            startActivityAndWait()

            MainCatalogPage(device).navigateToComponents()
            ComponentsPage(device).navigateToMediaComponents()
            MediaComponentsPage(device).navigateToMediaControlSheet()
        }
    ) {
        MediaControlSheetPage(device).expandSheet()
    }
}
