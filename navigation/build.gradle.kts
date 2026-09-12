plugins {
    id(libs.plugins.embarrasdf.kotlin.multiplatform.library.get().pluginId)
    id(libs.plugins.embarrasdf.compose.multiplatform.get().pluginId)
    id(libs.plugins.embarrasdf.kotlin.serialization.get().pluginId)
    id(libs.plugins.embarrasdf.maven.publish.get().pluginId)
}

kotlin {
    libraryTargets(
        androidNamespace = "com.embarrasdf.palette.navigation",
        iosFrameworkBaseName = "Navigation",
    )

    sourceSets {
        commonMain {
            dependencies {
                api(libs.navigation3.ui)
                api(libs.navigation3.runtime)
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}
