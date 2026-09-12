plugins {
    id(libs.plugins.embarrasdf.kotlin.multiplatform.library.get().pluginId)
    id(libs.plugins.embarrasdf.compose.multiplatform.get().pluginId)
    id(libs.plugins.embarrasdf.android.baselineprofile.consumer.library.get().pluginId)
    id(libs.plugins.embarrasdf.maven.publish.get().pluginId)
}

kotlin {
    libraryTargets(
        androidNamespace = "com.embarrasdf.palette.components",
        iosFrameworkBaseName = "Components",
    )

    sourceSets {
        commonMain {
            dependencies {
                api(libs.coil.compose)
                api(libs.coil.network.ktor3)
                api(libs.kotlinx.collections.immutable)
                api(libs.kotlinx.datetime)
                api(libs.trace)

                api(projects.formats)
                api(projects.modifiers)

                implementation(libs.compose.material.icons.extended)
            }
        }
        androidMain {
            dependencies {
                api(libs.ktor.client.android)
                api(libs.ktor.client.okhttp)
            }
        }
        appleMain {
            dependencies {
                api(libs.ktor.client.darwin)
            }
        }
        jvmMain {
            dependencies {
                api(libs.ktor.client.java)
            }
        }
    }
}

dependencies {
    baselineProfile(projects.components.baselineProfile)
}
