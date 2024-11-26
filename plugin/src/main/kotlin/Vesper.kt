package dev.triumphteam.vesper

import dev.triumphteam.nebula.ModularPlugin
import dev.triumphteam.nebula.module.modules
import dev.triumphteam.vesper.command.commands
import dev.triumphteam.vesper.command.menu.MenuOpenCommand
import dev.triumphteam.vesper.command.vesper.Test
import dev.triumphteam.vesper.config.MenuConfigManager

public class Vesper : ModularPlugin() {

    override val key: String = "vesper"

    override fun onStart() {

        modules {

            install(MenuConfigManager)
        }

        commands { plugin ->

            registerCommand(MenuOpenCommand())

            registerCommand(Test())
        }
    }
}
