rootProject.name = "chaoscraft"
include("chaoscraft-mod")
include("chaoscraft-control-center")
include("chaoscraft-datasource")
include("chaoscraft-api")
include("cilium-proto")

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net") { name = "Fabric" }
        mavenCentral()
        gradlePluginPortal()
        google()
    }
    plugins {
        val loomVersion: String by settings
        id("fabric-loom").version(loomVersion)
    }
}
