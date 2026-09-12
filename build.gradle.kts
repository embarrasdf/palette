plugins {
    alias(libs.plugins.embarrasdf.github.release)
}

subprojects {
    plugins.withId("app.cash.paparazzi") {
        // Defer until afterEvaluate so that testImplementation is created by Android plugin.
        afterEvaluate {
            dependencies.constraints {
                add("testImplementation", "com.google.guava:guava") {
                    attributes {
                        attribute(
                            TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
                            objects.named(TargetJvmEnvironment::class.java, TargetJvmEnvironment.STANDARD_JVM)
                        )
                    }
                    because("LayoutLib and sdk-common depend on Guava's -jre published variant." +
                            "See https://github.com/cashapp/paparazzi/issues/906.")
                }
            }
        }
    }
}

githubRelease {
    githubToken = System.getenv("GITHUB_TOKEN")
    repository = "embarrasdf/palette"
    enabled = !version.toString().endsWith("SNAPSHOT")
    newTagRevision = System.getenv("GITHUB_SHA")
}

tasks.register("generateAllBaselineProfiles") {
    group = "Baseline Profile"
    description = "Generates baseline profiles for app, components, and modifiers"

    dependsOn(
        ":app:baseline-profile:generateBaselineProfile",
        ":components:baseline-profile:generateBaselineProfile",
        ":formats:baseline-profile:generateBaselineProfile",
        ":modifiers:baseline-profile:generateBaselineProfile"
    )
}
