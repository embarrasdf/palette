plugins {
    id(libs.plugins.embarrasdf.android.baselineprofile.generator.get().pluginId)
}

android {
    namespace = "com.embarrasdf.palette.baselineprofile"

    targetProjectPath = ":app:androidApp"
}

firebaseTestLab {
    managedDevices {
        create(baselineProfileGenerator.deviceName) {
            device = baselineProfileGenerator.deviceType
            apiLevel = baselineProfileGenerator.apiLevel
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
    onVariants { v ->
        v.instrumentationRunnerArguments.put(
            "targetAppId",
            v.testedApks.map { v.artifacts.getBuiltArtifactsLoader().load(it)?.applicationId }
        )
    }
}
