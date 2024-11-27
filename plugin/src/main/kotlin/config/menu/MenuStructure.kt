package dev.triumphteam.vesper.config.menu

import dev.triumphteam.gui.item.GuiItem
import dev.triumphteam.gui.layout.BorderGuiLayout
import dev.triumphteam.gui.layout.GuiLayout
import dev.triumphteam.gui.paper.builder.item.ItemBuilder
import dev.triumphteam.gui.paper.container.type.ChestContainerType
import dev.triumphteam.gui.paper.container.type.PaperContainerType
import dev.triumphteam.gui.slot.Slot
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Serializable
public data class MenuConfig(
    @Contextual public val title: Component,
    public val container: MenuContainer,
    public val elements: List<MenuElement>,
)

@Serializable
public sealed interface MenuContainer {

    public fun asGuiContainer(): PaperContainerType

    @Serializable
    @SerialName("chest")
    public data class Chest(public val rows: Int) : MenuContainer {

        override fun asGuiContainer(): PaperContainerType = ChestContainerType(rows)
    }
}

@Serializable
public sealed interface MenuElement {

    @Serializable
    @SerialName("simple")
    public data class Simple(
        @Contextual public val slot: Slot,
        public val item: MenuItem,
    ) : MenuElement

    @Serializable
    @SerialName("filler")
    public data class Filler(
        public val layout: MenuLayout,
        public val item: MenuItem,
    ) : MenuElement
}

@Serializable
public sealed interface MenuLayout {

    public fun asGuiLayout(): GuiLayout

    @Serializable
    @SerialName("border")
    public data class Border(
        @Contextual public val min: Slot,
        @Contextual public val max: Slot,
    ) : MenuLayout {

        override fun asGuiLayout(): GuiLayout = BorderGuiLayout(min, max)
    }
}

@Serializable
public data class MenuItem(
    @Contextual public val material: Material,
    @Contextual public val name: Component? = null,
    public val lore: List<@Contextual Component> = emptyList(),
) {

    public fun asGuiItem(): GuiItem<Player, ItemStack> {
        val builder = ItemBuilder.from(material)

        name?.let { builder.name(it) }
        builder.lore(lore)

        return builder.asGuiItem()
    }
}
