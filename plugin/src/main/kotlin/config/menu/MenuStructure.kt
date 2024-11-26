package dev.triumphteam.vesper.config.menu

import dev.triumphteam.gui.kotlin.slot
import dev.triumphteam.gui.paper.container.type.ChestContainerType
import dev.triumphteam.gui.paper.container.type.PaperContainerType
import dev.triumphteam.gui.slot.Slot
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Material

@Serializable
public data class MenuConfig(
    public val title: String,
    public val container: MenuContainer,
    public val components: List<MenuComponent>,
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
public sealed interface MenuComponent {

    @Serializable
    @SerialName("simple")
    public data class Simple(
        public val elements: List<MenuElement>,
    ) : MenuComponent
}

@Serializable
public sealed interface MenuElement {

    public val slot: Slot

    @Serializable
    @SerialName("simple")
    public data class Simple(
        @Serializable(with = SlotSerializer::class) override val slot: Slot,
        @Serializable(with = MaterialSerializer::class) val material: Material,
    ) : MenuElement
}

public object MaterialSerializer : KSerializer<Material> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Material", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Material) {
        encoder.encodeString(value.name.lowercase())
    }

    override fun deserialize(decoder: Decoder): Material {
        val material = decoder.decodeString()
        return requireNotNull(Material.matchMaterial(material)) {
            "Material $material not found."
        }
    }
}

public object SlotSerializer : KSerializer<Slot> {

    private val INT_LIST_SERIALIZER = ListSerializer(Int.serializer())

    override val descriptor: SerialDescriptor = INT_LIST_SERIALIZER.descriptor

    override fun serialize(encoder: Encoder, value: Slot) {
        encoder.encodeSerializableValue(INT_LIST_SERIALIZER, listOf(value.row, value.column))
    }

    override fun deserialize(decoder: Decoder): Slot {
        val list = decoder.decodeSerializableValue(INT_LIST_SERIALIZER)
        require(list.size >= 2) {
            "Invalid Slot format!"
        }
        return slot(list[0], list[1])
    }
}
