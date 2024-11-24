package dev.triumphteam.vesper

import dev.triumphteam.nebula.ModularPlugin
import dev.triumphteam.nebula.module.modules
import dev.triumphteam.vesper.command.commands

public class Vesper : ModularPlugin() {

    override val key: String = "vesper"

    override fun onSetup() {

    }

    override fun onStart() {

        modules {

        }

        commands { plugin ->

            registerCommand()
        }
    }

    override fun onStop() {

    }
}
