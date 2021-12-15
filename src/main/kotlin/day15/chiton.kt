package day15

import inputLines
import splitIgnoreEmpty
import utils.Matrix
import utils.ShiftDirection
import java.lang.Integer.min


fun findWayThrough(map: Matrix<Int>): Matrix<Int> {
    var bestRisk = Int.MAX_VALUE

    fun handler(map: Matrix<Int>, acc: Matrix<Int?>): Matrix<Int> {
//        println("--------------------")
//        println(acc)
        val neighborTots = acc.zipN(
            acc.shift(ShiftDirection.UP, null),
            acc.shift(ShiftDirection.DOWN, null),
            acc.shift(ShiftDirection.LEFT, null),
            acc.shift(ShiftDirection.RIGHT, null),
        )
            .map { it.filterNotNull() }
        val newLowestRisk = map.zip(acc).zip(neighborTots).map { (loc, neighborsTot) ->
            val (risk, tot) = loc
            neighborsTot.minOrNull()
                ?.let { minNeighbor -> if (tot == null) minNeighbor + risk else min(tot, minNeighbor + risk) }
        }
        return if (newLowestRisk == acc) acc.map { it!! } else handler(map, newLowestRisk)
    }

    return handler(
        map,
        Matrix.fill<Int?>(map.nRows, map.nCols, null).set(0, 0, map.get(0, 0))
    )

}

fun main() {
    val input = inputLines("15", false)

    val map = Matrix(
        input.map { it.splitIgnoreEmpty("").map { it.toInt() } }
    )
    val p = findWayThrough(map)
    println(p.get(map.nRows - 1, map.nCols - 1) - map.get(0, 0))

    val f: (Int, Int) -> Int = { i, p -> (i - 1 + p) % 9 + 1 }
    val largeRow = map.plusCols(map.map { f(it, 1) }).plusCols(map.map { f(it, 2) }).plusCols(map.map { f(it, 3) })
        .plusCols(map.map { f(it, 4) })
    val largeMap = largeRow.plusRows(largeRow.map { f(it, 1) }).plusRows(largeRow.map { f(it, 2) })
        .plusRows(largeRow.map { f(it, 3) }).plusRows(largeRow.map { f(it, 4) })
    val pLarge= findWayThrough(largeMap)
    println(pLarge.get(largeMap.nRows - 1, largeMap.nCols - 1) - largeMap.get(0, 0))
}
