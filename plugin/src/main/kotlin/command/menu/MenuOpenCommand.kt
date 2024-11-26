package dev.triumphteam.vesper.command.menu

import com.typesafe.config.ConfigFactory
import dev.triumphteam.cmd.core.annotations.Command
import dev.triumphteam.cmd.core.annotations.Flag
import dev.triumphteam.cmd.core.annotations.Suggestion
import dev.triumphteam.cmd.core.argument.keyed.Flags
import dev.triumphteam.nebula.container.inject
import dev.triumphteam.vesper.config.MenuConfigManager
import dev.triumphteam.vesper.config.menu.MenuConfig
import dev.triumphteam.vesper.menu.ConfigurableMenu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import org.bukkit.entity.Player
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY


public class MenuOpenCommand : MenuCommand() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val menuConfigManager: MenuConfigManager by inject()

    @Command("open")
    @Flag(flag = "w", longFlag = "watch")
    public fun execute(player: Player, @Suggestion("menus") menu: String, flags: Flags) {

        val (value, file) = menuConfigManager[menu] ?: run {
            player.sendMessage("Menu doesn't exist!")
            return
        }

        val test = Hocon.decodeFromConfig<MenuConfig>(ConfigFactory.parseString(value))

        ConfigurableMenu(test).open(player)
        // coroutineScope.launch { watchAndOpen(player, file) }
    }

    private fun watchAndOpen(player: Player, file: Path) {
        /*val watchService = FileSystems.getDefault().newWatchService()
        file.register(watchService, ENTRY_MODIFY)

        while (true) {
            val key = watchService.take() ?: break

            for (event in key.pollEvents()) {
                player.sendMessage("Saved file.")
            }

            key.reset()
        }*/
    }
}
