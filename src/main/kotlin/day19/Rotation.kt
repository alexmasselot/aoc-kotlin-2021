package day19

// just the rotation matrix (only one of x* can be +/-1
// at creation, we cannot have two dimension hitting the same one (projection)
data class Rotation(
    val rx: Permutation,
    val ry: Permutation,
    val rz: Permutation
) {
    val fRotateX = fRotateOne(rx)
    val fRotateY = fRotateOne(ry)
    val fRotateZ = fRotateOne(rz)
    fun invert() = Rotation(rx.invert(), ry.invert(), rz.invert())

    fun apply(position: Position) = Position(
        fRotateX(position),
        fRotateY(position),
        fRotateZ(position),
    )

    fun apply(bs: Beacons) = Beacons(
        beacons = bs.beacons.map { Beacon(it.index, apply(it.pos)) }
    )

    fun turnX() = Rotation(rx, rz.invert(), ry)
    fun turnY() = Rotation(rz.invert(), ry, rx)
    fun turnZ() = Rotation(ry, rx.invert(), rz)


    override fun toString(): String = "(${rx.label},${ry.label},${rz.label})"

    companion object {
        fun fRotateOne(permutation: Permutation): (p: Position) -> Int =
            when (permutation) {
                Permutation.X_PLUS -> { p -> p.x }
                Permutation.X_MINUS -> { p -> -p.x }
                Permutation.Y_PLUS -> { p -> p.y }
                Permutation.Y_MINUS -> { p -> -p.y }
                Permutation.Z_PLUS -> { p -> p.z }
                Permutation.Z_MINUS -> { p -> -p.z }
            }


        val neutral = Rotation(Permutation.X_PLUS, Permutation.Y_PLUS, Permutation.Z_PLUS)

        // rotation, not rotation + symmetry
        val allRotations =
            (0..3).flatMap { ix ->
                (0..3).flatMap { iy ->
                    (0..3).map { iz -> listOf(ix, iy, iz) }
                }
            }.map {
                val (ix, iy, iz) = it
                val rotX = (0 until ix).fold(neutral) { acc, _ -> acc.turnX() }
                val rotY = (0 until iy).fold(rotX) { acc, _ -> acc.turnY() }
                (0 until iz).fold(rotY) { acc, _ -> acc.turnZ() }
            }.distinct()
    }
}
