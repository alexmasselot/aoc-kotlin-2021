package day19

import utils.Matrix

data class Beacons(
    val beacons: List<Beacon>
) {
    val relativeDeltas = Matrix(
        beacons.map { b1 ->
            beacons.map { b2 -> b1.pos.to(b2.pos) }
        }
    )
    private val deltaMap =
        beacons.flatMap { b1 ->
            beacons.filter { it.pos != b1.pos }.map { b2 ->
                Triple(b1.pos.to(b2.pos), b1, b2)
            }
        }

    fun overlapDelta(other: Beacons, minCount: Int): TranslationMatch? {
        val potentialTransitions = beacons.flatMap { b1 ->
            other.beacons.map { b2 -> Triple(b1.pos.to(b2.pos), b1, b2) }
        }
            .groupBy { it.first }
            .toList()
            .filter { it.second.size >= minCount }
            .map { it.first to it.second.map { t -> t.second to t.third } }
        if (potentialTransitions.size >= 2) {
            throw UnsupportedOperationException(potentialTransitions.toString())
        }

        return if (potentialTransitions.isEmpty()) {
            null
        } else {
            val first = potentialTransitions.first()
            TranslationMatch(
                first.first,
                first.second
            )
        }
    }

    override fun toString() = beacons.map { it.pos.toString() }.joinToString("\n")
}
