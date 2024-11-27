package dev.triumphteam.vesper.menu

import dev.triumphteam.gui.kotlin.set
import dev.triumphteam.gui.paper.builder.item.ItemBuilder
import dev.triumphteam.gui.paper.kotlin.builder.buildGui
import dev.triumphteam.nova.MutableState
import dev.triumphteam.nova.getValue
import dev.triumphteam.nova.mutableStateOf
import dev.triumphteam.vesper.config.menu.MenuConfig
import dev.triumphteam.vesper.config.menu.MenuElement
import dev.triumphteam.vesper.serialization.Serializer
import dev.triumphteam.vesper.serialization.decode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import java.nio.file.Path

private class WatchableMenu(
    /**
     * The job responsible for watching the file for changes.
     * This job **needs** to be canceled when the menu closes.
     */
    private val watcherJob: Job,
    /** The state that'll handle updating the menu when the config changes. */
    private val configState: MutableState<Result<MenuConfig>>,
) {

    private val gui = buildGui {

        // TODO(matt): MM and other stuff on title.
        title {
            val configResult by remember(configState)
            render {
                configResult.fold(
                    onSuccess = MenuConfig::title,
                    onFailure = { Component.text("Error on config file!", NamedTextColor.RED) }
                )
            }
        }


        // Container can't have states for now, so we don't allow it to be watched.
        configState.get().onSuccess { config ->
            containerType = config.container.asGuiContainer()
        }

        component {

            val configResult by remember(configState)

            render { container ->
                configResult.fold(
                    onSuccess = { config ->
                        config.elements.forEach { element ->
                            when (element) {
                                is MenuElement.Simple -> container[element.slot] = element.item.asGuiItem()
                                is MenuElement.Filler -> {
                                    element.layout.asGuiLayout().forEach { slot ->
                                        container[slot] = element.item.asGuiItem()
                                    }
                                }
                            }
                        }
                    },
                    onFailure = { throwable ->

                        val errorMessage = (throwable.message ?: "UNKNOWN").split(" ").chunked(4)
                            .map { Component.text(it.joinToString(" "), NamedTextColor.RED) }

                        container[1, 1] = ItemBuilder.from(Material.BARRIER).name(Component.text("An error occurred"))
                            .lore(errorMessage)
                            .asGuiItem()
                    }
                )
            }
        }

        // When the GUI closes, we have to cancel the job.
        onClose {
            // TODO(matt): For some reason this still doesn't cancel the job ??
            watcherJob.cancel()
        }
    }

    fun open(player: Player) {
        gui.open(player)
    }
}

public suspend fun Player.openWatchableMenu(configPath: Path) {

    // Config state with initial value.
    val configState = withContext(Dispatchers.IO) {
        mutableStateOf(Serializer.decode<MenuConfig>(configPath))
    }

    // The watcher job for the menu.
    val watcherJob = configPath.watch(CoroutineScope(Dispatchers.IO)) {
        // When the file updates, we update the state too.
        configState.set(Serializer.decode<MenuConfig>(it))
    }

    WatchableMenu(watcherJob, configState).open(this)
}
