import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

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

tasks {
    runServer {
        minecraftVersion("1.21.1")
    }

    bukkitPluginYaml {
        main.set("dev.triumphteam.vesper.Vesper")
        load = BukkitPluginYaml.PluginLoadOrder.STARTUP
        apiVersion = "1.21"
    }
}
