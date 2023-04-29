package eu.cafestube.cinematics.interpolation

interface InterpolationMode {

    fun interpolate(progress: Float, before: Double?, start: Double, end: Double, after: Double?) : Double {
        return interpolate(progress, start, end)
    }

    fun interpolate(progress: Float, start: Double, end: Double): Double { return 0.0 }

}