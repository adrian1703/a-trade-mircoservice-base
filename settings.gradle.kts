pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}
val PROJECT_NAME: String by settings
rootProject.name = PROJECT_NAME

include("a-trade-microservice-runtime-api")

project(":a-trade-microservice-runtime-api").name = "a-trade-microservice-runtime-api"