plugins {
    id(libs.plugins.embarrasdf.kotlin.multiplatform.library.get().pluginId)
    id(libs.plugins.embarrasdf.compose.multiplatform.get().pluginId)
    id(libs.plugins.embarrasdf.maven.publish.get().pluginId)
}

kotlin {
    libraryTargets(
        androidNamespace = "com.embarrasdf.palette.formats.demo",
        iosFrameworkBaseName = "FormatsDemo",
    )

    sourceSets {
        commonMain {
            dependencies {
                api(projects.components)
                api(projects.formats)
                api(projects.theme)
                api(projects.theme.components)

                implementation(libs.androidx.lifecycle.runtime.compose)
            }
        }
    }
}
