package dev.triumphteam.vesper.command.menu

import dev.triumphteam.cmd.core.annotations.Command
import dev.triumphteam.cmd.core.annotations.Flag
import dev.triumphteam.cmd.core.annotations.Suggestion
import dev.triumphteam.cmd.core.argument.keyed.Flags
import dev.triumphteam.nebula.container.inject
import dev.triumphteam.vesper.config.MenuConfigManager
import dev.triumphteam.vesper.menu.openWatchableMenu
import org.bukkit.entity.Player


public class MenuOpenCommand : MenuCommand() {

    private val menuConfigManager: MenuConfigManager by inject()

    @Command("open")
    @Flag(flag = "w", longFlag = "watch")
    public suspend fun execute(player: Player, @Suggestion("menus") menu: String, flags: Flags) {

        val (_, menuPath) = menuConfigManager[menu] ?: run {
            player.sendMessage("Menu doesn't exist!")
            return
        }

        player.openWatchableMenu(menuPath)
    }
}
