import org.gradle.accessors.dm.LibrariesForLibs

// Hack which exposes `libs` to this convention plugin
val libs = the<LibrariesForLibs>()

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("dev.triumphteam.root")
    id("xyz.jpenilla.run-paper")
    id("com.gradleup.shadow")
}

repositories {
    mavenCentral()
    maven("https://triumphteam.dev/snapshots/")
}

dependencies {
    compileOnly(libs.paper)
}

kotlin {
    explicitApi()
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
