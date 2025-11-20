plugins {
    java
    `maven-publish`
    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.springframework:spring-webflux:6.1.14")
    implementation("org.apache.avro:avro:1.11.4")
    implementation("org.slf4j:slf4j-api:2.0.9")
}

sourceSets["main"].java.srcDirs("src/main/avro")

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
