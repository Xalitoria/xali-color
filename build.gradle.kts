import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import mc.xalitoria.gradle.repo.reposilite

plugins {
    kotlin("jvm")
    id("mc.xalitoria.conventions")
    `maven-publish`
}

group = "mc.xalitoria"
version = "0.1.0"

dependencies {
    api(libs.mixbox)
    api(libs.bundles.adventure)
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
        reposilite(project, "https://maven.xali.host/releases")
    }
}