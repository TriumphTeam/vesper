import org.gradle.accessors.dm.LibrariesForLibs

// Hack which exposes `libs` to this convention plugin
val libs = the<LibrariesForLibs>()

plugins {
    kotlin("jvm")
    id("dev.triumphteam.root")
    id("xyz.jpenilla.run-paper")
    id("xyz.jpenilla.resource-factory-bukkit-convention")
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
}
