package day15

import inputLines
import splitIgnoreEmpty

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
data class Position(val x: Int, val y: Int) {
    override fun toString() = "$x,$y"
}

data class Path(val positions: List<Position>, val totalRisk: Int) {
    fun add(p: Position, risk: Int) = Path(positions.plus(p), totalRisk = totalRisk + risk)

    fun canAdd(p: Position) = !positions.contains(p)

    fun last() = positions.last()

    override fun toString() = "$totalRisk: " + positions.map { it.toString() }.joinToString("-")
}

data class ChitonMap(
    val risks: Array<IntArray>
) {
    val n = risks.size

    val start = Position(0, 0)
    val exit = Position(n - 1, n - 1)

    fun riskAt(p: Position) = risks[p.y][p.x]

    fun isInside(p: Position) =
        p.x >= 0 && p.y >= 0 && p.x < n && p.y < n

    fun neighborsOf(p: Position) =
        listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)
            .map { (i, j) -> Position(p.x + i, p.y + j) }
            .filter { isInside(it) }

    fun toString(path: Path) =
        risks.mapIndexed { y, row ->
            row.mapIndexed { x, r -> if (path.positions.contains(Position(x, y))) r else '.' }.joinToString("")
        }.joinToString("\n")


    companion object {
        fun read(input: List<String>) =
            ChitonMap(
                input
                    .filter { it.isNotBlank() }
                    .map { l ->
                        l.splitIgnoreEmpty("").map { it.toInt() }.toIntArray()
                    }
                    .toTypedArray()
            )
    }
}
data class SmallerRiskTo(
    val total: List<List<Int?>>
){

}

fun findWayThrough(map: ChitonMap): Path {
    var bestRisk = Int.MAX_VALUE

    fun handler(map: ChitonMap, acc: Path): Path? {
        if (acc.totalRisk >= bestRisk) {
            return null
        }
        if (acc.last() == map.exit) {
            println("$bestRisk // $acc")
            println(map.toString(acc))
            return if (acc.totalRisk < bestRisk) {
                acc
            } else {
                null
            }
        }
        val potential = map.neighborsOf(acc.last()).filter { acc.canAdd(it) }

        return potential.mapNotNull { nextPos ->
            val newPath = acc.add(nextPos, map.riskAt(nextPos))
            val p = handler(map, newPath)
            p?.let {
                if (it.totalRisk <= bestRisk) {
                    bestRisk = it.totalRisk
                    p
                } else {
                    null
                }
            }
        }.minByOrNull { it.totalRisk }
    }

    return handler(map, Path(listOf(map.start), 0))!!
}

fun main() {
    val input = inputLines("15", false)

    val map = ChitonMap.read(input)
    val p = findWayThrough(map)
    println(p)
}
