plugins {
    id("vesper.base")
}

dependencies {
    implementation(projects.vesperApi)

    implementation(libs.triumph.gui)
    implementation(libs.triumph.nebula)
    implementation(libs.bundles.triumph.commands)

    implementation(libs.serialization.hocon)
}

tasks {
    runServer {
        minecraftVersion("1.21.1")
    }
}
