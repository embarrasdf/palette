plugins {
    id(libs.plugins.embarrasdf.android.library.asProvider().get().pluginId)
    id(libs.plugins.embarrasdf.android.library.compose.get().pluginId)
    id(libs.plugins.embarrasdf.android.compose.test.get().pluginId)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "com.embarrasdf.palette.components"
}

dependencies {
    implementation(projects.components)
    implementation(projects.theme)
    testImplementation(libs.junit)
    testImplementation(libs.test.parameter.injector)
    testImplementation(projects.components)
    testImplementation(projects.theme)
    testImplementation(projects.testing)
}
