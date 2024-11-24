plugins {
    id("vesper.base")
}

dependencies {
    implementation(projects.vesperApi)

    implementation(libs.triumph.gui)
    implementation(libs.triumph.nebula)
    implementation(libs.triumph.commands)

    implementation(libs.serialization.hocon)
}
