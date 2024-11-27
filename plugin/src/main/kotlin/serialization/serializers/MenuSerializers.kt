package dev.triumphteam.vesper.serialization.serializers

import dev.triumphteam.gui.kotlin.slot
import dev.triumphteam.gui.slot.Slot
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material

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

public object MiniMessageSerializer : KSerializer<Component> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Component", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Component) {
        encoder.encodeString(MiniMessage.miniMessage().serialize(value))
    }

    override fun deserialize(decoder: Decoder): Component {
        return MiniMessage.miniMessage().deserialize(decoder.decodeString())
    }
}
