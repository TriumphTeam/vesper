package dev.triumphteam.vesper

import dev.triumphteam.nebula.ModularPlugin
import dev.triumphteam.nebula.module.modules
import dev.triumphteam.vesper.command.commands
import dev.triumphteam.vesper.command.vesper.Test

public class Vesper : ModularPlugin() {

    override val key: String = "vesper"

    override fun onSetup() {

    }

    override fun onStart() {

        modules {

        }

        commands { plugin ->

            registerCommand(Test())
        }
    }

    override fun onStop() {

    }
}
