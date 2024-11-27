import dev.triumphteam.root.KotlinOpt
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

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

root {
    configureKotlin {
        explicitApi()
        jvmVersion(21)
        optIn(KotlinOpt.ALL)
    }
}
