package day19

data class RotationTranslationMatch(
    val scannerSource: Scanner,
    val scannerTarget: Scanner,
    val rotationTarget: Rotation,
    val translation: Translation,
    val matchingBeacons: List<Pair<Beacon, Beacon>>
) {
    fun invert(beacons: Beacons) =
        translation.invert().apply(rotationTarget.invert().apply(beacons))

    override fun toString() = "${scannerSource.id} -> ${scannerTarget.id}: $translation, $rotationTarget"
}
