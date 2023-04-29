package eu.cafestube.cinematics.interpolation

object LinearInterpolation : InterpolationMode {

    override fun interpolate(progress: Float, start: Double, end: Double): Double {
        return start + (end - start) * progress
    }

}