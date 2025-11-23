plugins {
    `maven-publish`
    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"
    kotlin("jvm") version "2.2.10"
    kotlin("plugin.allopen") version "2.2.10"
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.4"
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://packages.confluent.io/maven/")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        freeCompilerArgs.set(listOf("-Xannotation-default-target=param-property"))
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
        javaParameters = true
    }
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}
tasks.getByName<Jar>("jar") {
    enabled = true
}

dependencies {
    implementation("org.springframework:spring-webflux")
    implementation("org.apache.avro:avro:1.11.4")
    implementation("org.apache.kafka:kafka-clients:4.1.0")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("net.jcip:jcip-annotations:1.0")
    implementation("org.springframework:spring-context")

}

sourceSets["main"].java.srcDirs("src/main/avro")

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
