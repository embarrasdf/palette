plugins {
    id(libs.plugins.embarrasdf.android.library.asProvider().get().pluginId)
    id(libs.plugins.embarrasdf.android.library.compose.get().pluginId)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "com.embarrasdf.palette.modifiers"
}

dependencies {
    implementation(projects.modifiers)
    testImplementation(libs.junit)
    testImplementation(libs.test.parameter.injector)
    testImplementation(projects.testing)
}
