package day19

import kotlin.math.abs


data class Translation(
    val vx: Int,
    val vy: Int,
    val vz: Int
) {
    fun invert() = Translation(-vx, -vy, -vz)
    fun apply(pos: Position) = Position(pos.x + vx, pos.y + vy, pos.z + vz)
    fun apply(bs: Beacons) = Beacons(
        bs.beacons.map { Beacon(it.index, apply(it.pos)) }
    )

    fun manhattan() = abs(vx) + abs(vy) + abs(vz)

    override fun toString() = "($vx,$vy,$vz)"

    companion object {
        val neutral = Translation(0, 0, 0)
    }
}
