rootProject.name = "bo-portal-service"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

include(
    "common",
    "core",
    "infra",
    "api"
)
