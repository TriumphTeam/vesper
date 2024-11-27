package dev.triumphteam.vesper.menu

import io.methvin.watcher.DirectoryChangeEvent
import io.methvin.watcher.DirectoryWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.name

/**
 * Watch the given path on the given scope.
 * The [onChange] will be called every time there is a modification on the file.
 */
public fun Path.watch(
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    onChange: (Path) -> Unit,
): Job {

    // Make sure it's a file, not a directory.
    require(!isDirectory()) {
        "Cannot watch directories!"
    }

    // Launch a new job in the given scope.
    return scope.launch {
        DirectoryWatcher.builder()
            .path(parent) // Parent of the current file being watched.
            .listener { event ->
                when (event.eventType()) {
                    DirectoryChangeEvent.EventType.MODIFY -> {
                        // We only care about this path.
                        if (event.path().name != name) return@listener

                        onChange(this@watch)
                    }

                    else -> {} // Ignore anything that isn't modification
                }
            }.build().watch()
    }
}
