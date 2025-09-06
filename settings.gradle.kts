pluginManagement {
    val quarkusPluginVersion: String by settings
    val quarkusPluginId: String by settings
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {
        id(quarkusPluginId) version quarkusPluginVersion
    }
}
rootProject.name = "a-trade-mircoservice-base"

include("a-trade-microservice-runtime-api")

project(":a-trade-microservice-runtime-api").name = "a-trade-microservice-runtime-api"