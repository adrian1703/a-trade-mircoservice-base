plugins {
    java
    `maven-publish`
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
    compileOnly("org.springframework:spring-webflux:6.1.14")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
