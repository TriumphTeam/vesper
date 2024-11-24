package dev.triumphteam.vesper.command.vesper

import dev.triumphteam.cmd.core.annotations.Command
import org.bukkit.command.CommandSender

public class Test : VesperCommand() {

    @Command
    public fun execute(sender: CommandSender) {
        sender.sendMessage("This is a test")
    }
}
