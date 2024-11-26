package dev.triumphteam.vesper.menu

import dev.triumphteam.gui.kotlin.set
import dev.triumphteam.gui.paper.builder.item.ItemBuilder
import dev.triumphteam.gui.paper.kotlin.builder.PaperKotlinGuiBuilder
import dev.triumphteam.gui.paper.kotlin.builder.buildGui
import dev.triumphteam.vesper.config.menu.MenuComponent
import dev.triumphteam.vesper.config.menu.MenuConfig
import dev.triumphteam.vesper.config.menu.MenuElement
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

public class ConfigurableMenu(config: MenuConfig) {

    private val gui = buildGui {

        // TODO(matt): MM and other stuff on title.
        title(Component.text(config.title))

        // Container can't have states for now, so we don't allow it to be watched.
        containerType = config.container.asGuiContainer()

        config.components.forEach { component ->
            when (component) {
                is MenuComponent.Simple -> simpleComponent(component)
            }
        }
    }

    private fun PaperKotlinGuiBuilder.simpleComponent(component: MenuComponent.Simple) {
        component {
            render { container ->

                component.elements.forEach { element ->

                    when (element) {
                        is MenuElement.Simple -> {
                            container[element.slot] = ItemBuilder.from(element.material).asGuiItem()
                        }
                    }
                }
            }
        }
    }

    public fun open(player: Player) {
        gui.open(player)
    }
}
