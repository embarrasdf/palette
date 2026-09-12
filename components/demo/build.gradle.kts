plugins {
    id(libs.plugins.embarrasdf.kotlin.multiplatform.library.get().pluginId)
    id(libs.plugins.embarrasdf.compose.multiplatform.get().pluginId)
    id(libs.plugins.embarrasdf.maven.publish.get().pluginId)
}

kotlin {
    libraryTargets(
        androidNamespace = "com.embarrasdf.palette.components.demo",
        iosFrameworkBaseName = "ComponentsDemo",
    )

    sourceSets {
        commonMain {
            dependencies {
                api(projects.components)
                api(projects.formats.demo)
                api(projects.theme)
                api(projects.theme.components)

                implementation(libs.compose.material.icons.extended)
            }
        }
    }
}
