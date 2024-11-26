package dev.triumphteam.vesper.command

import dev.triumphteam.cmd.bukkit.BukkitCommandManager
import dev.triumphteam.cmd.core.extention.ExtensionBuilder
import dev.triumphteam.cmd.core.suggestion.SuggestionKey
import dev.triumphteam.cmds.useCoroutines
import dev.triumphteam.nebula.container.inject
import dev.triumphteam.vesper.Vesper
import dev.triumphteam.vesper.config.MenuConfigManager
import org.bukkit.command.CommandSender

public inline fun Vesper.commands(block: BukkitCommandManager<CommandSender>.(plugin: Vesper) -> Unit) {
    val manager = BukkitCommandManager.create(this) { builder ->
        builder.extensions(ExtensionBuilder<CommandSender, CommandSender>::useCoroutines)
    }

    val menusConfigManager: MenuConfigManager by inject()

    manager.registerSuggestion(SuggestionKey.of("menus")) { _, _ ->
        menusConfigManager.menuKeys.toList()
    }

    block(manager, this)
}
