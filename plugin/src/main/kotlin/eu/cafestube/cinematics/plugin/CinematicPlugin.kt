package eu.cafestube.cinematics.plugin

import org.bukkit.plugin.java.JavaPlugin

class CinematicPlugin : JavaPlugin() {

    init {
        server.commandMap.register(pluginMeta.name, CinematicCommand(this))

    }

}