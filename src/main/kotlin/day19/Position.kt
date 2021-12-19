package day19

data class Position(
    val x: Int,
    val y: Int,
    val z: Int
) {
    fun to(other: Position) = Translation(other.x - x, other.y - y, other.z - z)
    override fun toString() = "($x,$y,$z)"
}
