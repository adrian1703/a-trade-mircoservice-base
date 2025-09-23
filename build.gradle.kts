plugins {
    kotlin("jvm") version "2.2.10"
    kotlin("plugin.allopen") version "2.2.10"
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.4"
}

repositories {
    mavenCentral()
    mavenLocal()
}


dependencies {
    implementation(project(":a-trade-microservice-runtime-api"))
    implementation("net.jcip:jcip-annotations:1.0")

    implementation("org.apache.kafka:kafka-clients:4.1.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.apache.avro:avro:1.11.4")
    // Optionally, for dev tools or test support:
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.testcontainers:testcontainers:1.21.3")
    testImplementation("org.testcontainers:junit-jupiter:1.21.3")
}

group = "adrian.kuhn"
version = "0.0.1"

subprojects {
    group = "adrian.kuhn"
    version = "0.0.1"
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-XX:+EnableDynamicAgentLoading")
    exclude("integrationTest/*")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

//tasks.withType<Test> {
//    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
//    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
//}
allOpen {
    annotation("org.springframework.context.annotation.Bean")
    annotation("org.springframework.context.annotation.Configuration")
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
        freeCompilerArgs.set(listOf("-Xannotation-default-target=param-property"))
        javaParameters = true
    }
}

// =================================== Plugin Contender ===================================
sourceSets {
    create("integration")
}

sourceSets["integration"].compileClasspath += sourceSets["main"].output
sourceSets["integration"].runtimeClasspath += sourceSets["main"].output

configurations["integrationImplementation"].extendsFrom(configurations.implementation.get())
configurations["integrationRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

tasks.register<JavaExec>("runIntegration") {
    group = "application"
    description = "Run a main from the integration source set"
    classpath = sourceSets["integration"].runtimeClasspath
    // Set your FQ main class here:
    mainClass.set("integrationTest.a.trading.microservice.base.IntegrationTestRunnerKt")
}
// ========================================================================================