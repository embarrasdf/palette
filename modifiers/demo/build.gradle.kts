plugins {
    id(libs.plugins.embarrasdf.kotlin.multiplatform.library.get().pluginId)
    id(libs.plugins.embarrasdf.compose.multiplatform.get().pluginId)
    id(libs.plugins.embarrasdf.maven.publish.get().pluginId)
}

kotlin {
    libraryTargets(
        androidNamespace = "com.embarrasdf.palette.modifiers.demo",
        iosFrameworkBaseName = "ModifiersDemo",
    )

    sourceSets {
        commonMain {
            dependencies {
                api(projects.components.demo)
                api(projects.modifiers)
                api(projects.theme)
                api(projects.theme.components)
            }
        }
    }
}
