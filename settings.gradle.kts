pluginManagement {
    if (file("../gradle-plugins").exists()) {
        includeBuild("../gradle-plugins")
    }
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }
    versionCatalogs {
        if (file("../gradle-plugins").exists()) {
            create("embarrasdfPluginLibs") {
                from(files("../gradle-plugins/gradle/libs.versions.toml"))
            }
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "palette"

include(":app:androidApp")
include(":app:baseline-profile")
include(":app:benchmark")
include(":app:composeApp")
include(":app:desktopApp")
include(":app:uiautomator-fixtures")
include(":app:webApp")
include(":components")
include(":components:android-test")
include(":components:baseline-profile")
include(":components:demo")
include(":formats")
include(":formats:baseline-profile")
include(":formats:demo")
include(":modifiers")
include(":modifiers:android-test")
include(":modifiers:baseline-profile")
include(":modifiers:demo")
include(":navigation")
include(":testing")
include(":theme")
include(":theme:components")

val localPropsFile = rootDir.resolve("local.properties").takeIf { it.exists() }
val localProps = java.util.Properties().apply {
    localPropsFile?.inputStream()?.use { load(it) }
}

val includeLogging = localProps.getProperty("includeLogging")?.toBoolean() ?: false
if (includeLogging && file("../logging").exists()) {
    includeBuild("../logging") {
        dependencySubstitution {
            substitute(module("com.embarrasdf.logging:logger-api")).using(project(":logger-api"))
            substitute(module("com.embarrasdf.logging:logger-impl")).using(project(":logger-impl"))
            substitute(module("com.embarrasdf.logging:loggable")).using(project(":loggable"))
        }
    }
}

val includeTrace = localProps.getProperty("includeTrace")?.toBoolean() ?: false
if (includeTrace && file("../trace").exists()) {
    includeBuild("../trace") {
        dependencySubstitution {
            substitute(module("com.embarrasdf.trace:trace")).using(project(":trace"))
        }
    }
}

val includeUievent = localProps.getProperty("includeUievent")?.toBoolean() ?: false
if (includeUievent && file("../uievent").exists()) {
    includeBuild("../uievent") {
        dependencySubstitution {
            substitute(module("com.embarrasdf.uievent:uievent")).using(project(":uievent"))
        }
    }
}
