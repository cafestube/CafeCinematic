package eu.cafestube.cinematics.interpolation

object CatmullRomInterpolation : InterpolationMode {

    override fun interpolate(progress: Float, before: Double?, start: Double, end: Double, after: Double?): Double {
        return catmullRom(before ?: start, start, end, after ?: end, progress)
    }

    private fun catmullRom(p0: Double, p1: Double, p2: Double, p3: Double, t: Float): Double {
        return 0.5 * ((2 * p1) + (-p0 + p2) * t + (2 * p0 - 5 * p1 + 4 * p2 - p3) * t * t + (-p0 + 3 * p1 - 3 * p2 + p3) * t * t * t);
    }
}