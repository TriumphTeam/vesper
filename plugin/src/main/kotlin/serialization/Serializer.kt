package dev.triumphteam.vesper.serialization

import com.typesafe.config.ConfigFactory
import dev.triumphteam.vesper.serialization.serializers.MaterialSerializer
import dev.triumphteam.vesper.serialization.serializers.MiniMessageSerializer
import dev.triumphteam.vesper.serialization.serializers.SlotSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.serializer
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.readText

/** Common interface between different types of serializers. */
public interface Serializer {

    /** The common serializer implementation will be generic and contextual. */
    public companion object : Serializer {

        // Supported extensions
        private const val HOCON_EXTENSION = "conf"
        private const val YML_EXTENSION = "yml"
        private const val YAML_EXTENSION = "yaml"

        /** The common serializers module for sharing contextual serializers. */
        private val serializersModule = SerializersModule {
            contextual(SlotSerializer)
            contextual(MaterialSerializer)
            contextual(MiniMessageSerializer)
        }

        /**
         * Contextual decode function.
         * The serializer used is based on the extension of the given file.
         */
        override fun <T> decode(serializer: KSerializer<T>, path: Path): Result<T> {
            return when (val extension = path.extension) {
                HOCON_EXTENSION -> Hocon.decode(serializer, path)
                YML_EXTENSION, YAML_EXTENSION -> Yaml.decode(serializer, path)
                else -> throw SerializationException("Unsupported extension: $extension")
            }
        }
    }

    public fun <T> decode(serializer: KSerializer<T>, path: Path): Result<T>

    public fun <T> decodeOrThrow(serializer: KSerializer<T>, path: Path): T =
        decode(serializer, path).getOrThrow()

    /** Support for HOCON serialization. */
    public object Hocon : Serializer {

        private val hocon = Hocon {
            serializersModule = Serializer.serializersModule
        }

        override fun <T> decode(serializer: KSerializer<T>, path: Path): Result<T> = runCatching {
            hocon.decodeFromConfig(serializer, ConfigFactory.parseString(path.readText()))
        }
    }

    /** Support for YAML serialization. */
    public object Yaml : Serializer {

        override fun <T> decode(serializer: KSerializer<T>, path: Path): Result<T> {
            return TODO("Yaml is not yet supported.")
        }
    }
}

/**
 * Reified version of the decode function so that the serializer isn't needed.
 * Return should be based on the serializer type or the generic from [Serializer.Companion].
 */
public inline fun <reified T : Any> Serializer.decode(path: Path): Result<T> =
    decode(T::class.serializer(), path)
