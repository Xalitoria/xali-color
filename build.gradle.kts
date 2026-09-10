import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `maven-publish`
}

group = "mc.xalitoria"
version = "0.0.2"

dependencies {
    implementation(libs.mixbox)
    testImplementation(kotlin("test"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
    disableAutoTargetJvm()
}

kotlin {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
    jvmToolchain(8)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven("https://maven.xali.host/releases") {
            credentials {
                username = System.getenv("MAVEN_USER") ?: project.findProperty("maven.user") as? String
                password = System.getenv("MAVEN_TOKEN") ?: project.findProperty("maven.token") as? String
            }
        }
    }
}

tasks {

}