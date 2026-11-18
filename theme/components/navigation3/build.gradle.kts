plugins {
    id(libs.plugins.embarrasdf.kotlin.multiplatform.library.get().pluginId)
    id(libs.plugins.embarrasdf.compose.multiplatform.get().pluginId)
    id(libs.plugins.embarrasdf.maven.publish.get().pluginId)
}

kotlin {
    libraryTargets(
        androidNamespace = "com.embarrasdf.palette.theme.components.navigation3",
        iosFrameworkBaseName = "ThemeComponentsNavigation3",
    )

    sourceSets {
        commonMain {
            dependencies {
                api(projects.theme)
                api(libs.navigation3.ui)
                api(libs.navigation3.runtime)
            }
        }
    }
}
