package eu.cafestube.cinematics.interpolation

object NoInterpolation : InterpolationMode {

    override fun interpolate(progress: Float, start: Double, end: Double): Double {
        return start
    }

}