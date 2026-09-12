plugins {
    id(libs.plugins.embarrasdf.android.benchmark.get().pluginId)
}

android {
    namespace = "com.embarrasdf.palette.benchmark"

    buildTypes {
        create("benchmarkRelease") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
        }
    }

    targetProjectPath = ":app:androidApp"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

firebaseTestLab {
    managedDevices {
        create(benchmark.deviceName) {
            device = benchmark.deviceType
            apiLevel = benchmark.apiLevel
        }
    }
    val serviceAccountJson = System.getenv("FIREBASE_TEST_LAB_SERVICE_ACCOUNT")
    if (serviceAccountJson != null) {
        serviceAccountCredentials.set(file(serviceAccountJson))
    }
    testOptions {
        results.cloudStorageBucket = providers.gradleProperty("palette.ftl.bucket").get()
    }
}

dependencies {
    implementation(projects.app.uiautomatorFixtures)
}

androidComponents {
    beforeVariants(selector().all()) {
        it.enable = it.buildType == "benchmarkRelease"
    }
}
