package dev.triumphteam.vesper.command

import dev.triumphteam.cmd.bukkit.BukkitCommandManager
import dev.triumphteam.vesper.Vesper
import org.bukkit.command.CommandSender

public inline fun Vesper.commands(block: BukkitCommandManager<CommandSender>.(plugin: Vesper) -> Unit) {
    block(BukkitCommandManager.create(this), this)
}
