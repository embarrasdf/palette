plugins {
    id(libs.plugins.embarrasdf.kotlin.multiplatform.library.get().pluginId)
    id(libs.plugins.embarrasdf.kotlin.serialization.get().pluginId)
    id(libs.plugins.embarrasdf.compose.multiplatform.get().pluginId)
}

kotlin {
    libraryTargets(
        androidNamespace = "com.embarrasdf.palette.app",
        iosFrameworkBaseName = "App",
    )

    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.trace)
            implementation(libs.uievent)

            implementation(projects.components)
            implementation(projects.components.demo)
            implementation(projects.formats)
            implementation(projects.formats.demo)
            implementation(projects.modifiers)
            implementation(projects.modifiers.demo)
            implementation(projects.navigation)
            implementation(projects.theme)
            implementation(projects.theme.components)
        }
    }
}
