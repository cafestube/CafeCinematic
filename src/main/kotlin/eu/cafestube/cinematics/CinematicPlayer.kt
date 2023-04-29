package eu.cafestube.cinematics

import io.papermc.paper.entity.TeleportFlag
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

class CinematicPlayer(
    private val plugin: JavaPlugin,
    val frames: List<KeyFrame>,
    var speedModifier: Float = 1.0f
): Runnable {

    val players = ArrayList<Player>()

    private var time: Double = 0.0
    private var lastFrameFinished: Int = 0

    private lateinit var bukkitTask: BukkitTask
    private val endListeners = ArrayList<Runnable>()

    override fun run() {
        if(frames.isEmpty()) {
            cancel()
            return
        }

        this.time += 1.0 * speedModifier

        runTick()
    }

    fun schedule() {
        if(isRunning()) return
        this.bukkitTask = plugin.server.scheduler.runTaskTimer(plugin, this, 0, 1)
    }

    fun addEndListener(runnable: Runnable) {
        endListeners.add(runnable)
    }

    fun cancel() {
        if(!isRunning()) throw IllegalStateException("Cinematic is not running")

        bukkitTask.cancel()
        endListeners.forEach { it.run() }
    }

    fun isRunning(): Boolean {
        if(!::bukkitTask.isInitialized) return false
        return !bukkitTask.isCancelled
    }

    private fun runTick() {
        if (this.lastFrameFinished >= frames.size-1) {
            cancel()
            return
        }

        val frameFrom = frames[this.lastFrameFinished]
        if(frameFrom == frames.last()) {
            cancel()
            return
        }
        val frameTo = frames[this.lastFrameFinished+1]

        if(time >= frameTo.tick) {
            this.lastFrameFinished++
            return runTick()
        }

        val frameBefore = if(lastFrameFinished > 0) frames[this.lastFrameFinished-1] else null
        val next = if(lastFrameFinished+2 >= frames.size) null else frames[this.lastFrameFinished+2]

        val progress: Float = ((time - frameFrom.tick.toDouble()) / (frameTo.tick - frameFrom.tick.toDouble())).toFloat()

        val frame = Location(
            frameTo.position.world,
            frameTo.interpolationMode.interpolate(
                progress,
                before = frameBefore?.position?.x,
                start = frameFrom.position.x,
                end = frameTo.position.x,
                after = next?.position?.x
            ),
            frameTo.interpolationMode.interpolate(
                progress,
                before = frameBefore?.position?.y,
                start = frameFrom.position.y,
                end = frameTo.position.y,
                after = next?.position?.y
            ),
            frameTo.interpolationMode.interpolate(
                progress,
                before = frameBefore?.position?.z,
                start = frameFrom.position.z,
                end = frameTo.position.z,
                after = next?.position?.z
            ),
            (frameTo.interpolationMode.interpolate(
                progress,
                before = frameBefore?.position?.yaw?.toDouble(),
                start = frameFrom.position.yaw.toDouble(),
                end = frameTo.position.yaw.toDouble(),
                after = next?.position?.yaw?.toDouble()
            ) % 360).toFloat(),
            (frameTo.interpolationMode.interpolate(
                progress,
                before = frameBefore?.position?.pitch?.toDouble(),
                start = frameFrom.position.pitch.toDouble(),
                end = frameTo.position.pitch.toDouble(),
                after = next?.position?.pitch?.toDouble()
            ) % 360).toFloat(),
        )

        players.removeIf { !it.isOnline }

        players.forEach {
            if(!it.allowFlight || !it.isFlying) {
                it.allowFlight = true
                it.isFlying = true
            }

            it.teleport(frame, TeleportFlag.Relative.X, TeleportFlag.Relative.Y, TeleportFlag.Relative.Z,
                TeleportFlag.Relative.YAW, TeleportFlag.Relative.PITCH, TeleportFlag.EntityState.RETAIN_OPEN_INVENTORY)
        }
    }




}