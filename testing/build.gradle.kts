plugins {
    id(libs.plugins.embarrasdf.android.library.asProvider().get().pluginId)
    id(libs.plugins.embarrasdf.android.library.compose.get().pluginId)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "com.embarrasdf.palette.testing"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(libs.junit)
    api(libs.paparazzi)
}
