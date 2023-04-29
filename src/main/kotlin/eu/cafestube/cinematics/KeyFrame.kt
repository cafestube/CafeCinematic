package eu.cafestube.cinematics

import eu.cafestube.cinematics.interpolation.InterpolationMode
import org.bukkit.Location

data class KeyFrame(val position: Location, val tick: Int, val interpolationMode: InterpolationMode)