plugins {
    id(libs.plugins.embarrasdf.kotlin.multiplatform.library.get().pluginId)
    id(libs.plugins.embarrasdf.compose.multiplatform.get().pluginId)
    id(libs.plugins.embarrasdf.maven.publish.get().pluginId)
}

kotlin {
    libraryTargets(
        androidNamespace = "com.embarrasdf.palette.theme.components",
        iosFrameworkBaseName = "ThemeComponents",
    )

    sourceSets {
        commonMain {
            dependencies {
                api(projects.components)
                api(projects.theme)
            }
        }
    }
}
