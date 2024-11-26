package dev.triumphteam.vesper.config

import dev.triumphteam.nebula.module.BaseModule
import dev.triumphteam.nebula.module.ModuleFactory
import dev.triumphteam.vesper.Vesper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.createDirectories

public data class MenuConfigHolder(
    public val value: String,
    public val file: Path,
)

public class MenuConfigManager(public val dataDirectory: Path) : BaseModule() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val menus: MutableMap<String, MenuConfigHolder> = ConcurrentHashMap()

    public val menuKeys: Set<String>
        get() = menus.keys

    init {
        onRegister {
            coroutineScope.launch {
                loadMenus()
            }
        }
    }

    public operator fun get(key: String): MenuConfigHolder? {
        return menus[key]
    }

    private fun loadMenus() {
        val menusDirectory = dataDirectory.toFile().also {
            require(it.isDirectory) {
                "An error occurred while loading menus, make sure the 'menus' directory exists."
            }
        }

        menusDirectory.listFiles()?.forEach { file ->
            menus[file.nameWithoutExtension] = MenuConfigHolder(file.readText(), file.toPath())
        }
    }

    public companion object : ModuleFactory<MenuConfigManager, Vesper> {

        override fun install(container: Vesper): MenuConfigManager {
            return MenuConfigManager(container.dataPath.resolve("menus").also { it.createDirectories() })
        }
    }
}
