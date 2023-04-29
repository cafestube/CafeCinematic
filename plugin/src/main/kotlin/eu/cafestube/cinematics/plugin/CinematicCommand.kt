package eu.cafestube.cinematics.plugin

import eu.cafestube.cinematics.CinematicPlayer
import eu.cafestube.cinematics.KeyFrame
import eu.cafestube.cinematics.interpolation.CatmullRomInterpolation
import eu.cafestube.cinematics.interpolation.LinearInterpolation
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CinematicCommand(private val plugin: CinematicPlugin) : Command("cinematic") {

    private val frames = ArrayList<KeyFrame>()

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
        if(args.size == 2) {
            if(args[0] == "add-point") {
                frames.add(KeyFrame((sender as Player).location, args[1].toInt(), CatmullRomInterpolation))
            }
        }
        if(args.size == 1) {
            if(args[0] == "play") {
                val cinematicPlayer = CinematicPlayer(plugin, frames)
                cinematicPlayer.players.add(sender as Player)
                cinematicPlayer.schedule()
            }
        }
        return true
    }

}