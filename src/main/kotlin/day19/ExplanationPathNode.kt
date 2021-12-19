package day19

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
class ExplanationPathNode(
    val scanner: Scanner,
    val translation: Translation,
    val rotation: Rotation
) {
    private val children = mutableListOf<ExplanationPathNode>()
    fun add(node: ExplanationPathNode) {
        children.add(node)
    }

    fun reduceBeacons(): List<Position> {
        val childPosition = children.flatMap { child ->
            child.reduceBeacons()
        }
        return scanner.beacons.beacons.map { it.pos }
            .plus(childPosition)
            .map {
                translation.invert().apply(
                    rotation.apply(it)
                )
            }.distinct()
    }

    fun scannerPositions(): List<Position> {
        val childPosition = children.flatMap { child ->
            child.scannerPositions()
        }
        return listOf(Position(0, 0, 0)).plus(childPosition)
            .map {
                translation.invert().apply(
                    rotation.apply(it)
                )
            }
    }

    fun toString(prefix: String): String {
        return "$prefix ${scanner.id} translation=$translation rotation=$rotation" +
            (if (children.isEmpty()) "" else "\n") +
            children.map { it.toString("$prefix  ") }.joinToString("\n")
    }

    companion object {
        fun build(matches: List<RotationTranslationMatch>): ExplanationPathNode {
            fun handler(
                node: ExplanationPathNode,
                matches: List<RotationTranslationMatch>
            ): List<RotationTranslationMatch> {
                if (matches.isEmpty()) {
                    return emptyList()
                }
                val (here: List<RotationTranslationMatch>, remain: List<RotationTranslationMatch>) =
                    matches.filter { it.scannerTarget != node.scanner }.partition { it.scannerSource == node.scanner }
                return here.fold(remain) { dec, m ->
                    val child = ExplanationPathNode(
                        m.scannerTarget,
                        m.translation,
                        m.rotationTarget
                    )
                    node.add(
                        child
                    )
                    handler(child, dec)
                }
            }

            val root = ExplanationPathNode(
                matches.first().scannerSource,
                Translation.neutral,
                Rotation.neutral
            )
            handler(
                root,
                matches
            )
            return root
        }
    }
}
