package day19

data class RotatedScanner(
    val id: Int,
    val rotation: Rotation,
    val beacons: Beacons
) {
    companion object {
        fun generate(scanner: Scanner) = Rotation.allRotations.map { rotation ->
            RotatedScanner(
                scanner.id,
                rotation,
                Beacons(scanner.beacons.beacons.map { b -> Beacon(b.index, rotation.apply(b.pos)) })
            )
        }
    }
}
