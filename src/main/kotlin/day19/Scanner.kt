package day19

import splitIgnoreEmpty

data class Scanner(
    val id: Int,
    val beacons: Beacons
) {
    fun findMatchingRotated(other: Scanner, minCount: Int): RotationTranslationMatch? {
        val rotTransMatches = RotatedScanner.generate(other).mapNotNull { rotatedScanner ->
            beacons.overlapDelta(rotatedScanner.beacons, minCount)?.let {
                RotationTranslationMatch(
                    this,
                    other,
                    rotatedScanner.rotation,
                    it.translation,
                    it.matchingBeacons
                )
            }
        }
        if (rotTransMatches.size > 1) {
            throw UnsupportedOperationException(rotTransMatches.toString())
        }
        return rotTransMatches.firstOrNull()
    }

    companion object {
        fun buildScanner(str: String): Scanner {
            val lines = str.splitIgnoreEmpty("\n")
            return Scanner(
                lines.first().replace("--- scanner ", "").replace(" ---", "").toInt(),
                Beacons(
                    lines.drop(1).mapIndexed { i, l ->
                        val (x, y, z) = l.split(",").map { it.toInt() }
                        Beacon(i, Position(x, y, z))
                    }
                )
            )
        }

        fun buildScanners(input: List<String>): List<Scanner> =
            input.joinToString("\n").split("\n\n")
                .map { buildScanner(it) }
    }
}
