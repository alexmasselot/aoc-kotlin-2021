package day13

import inputLines
import java.lang.Integer.max
import java.lang.Integer.min

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
data class Map(
    val nCols: Int,
    val nRows: Int,
    val dots: List<BooleanArray>
) {
    fun countDots() =
        dots.map {
            it.filter { it }.size
        }.sum()

    override fun toString() =
        dots.map { row ->
            row.map {
                if (it) {
                    "#"
                } else {
                    "."
                }
            }.joinToString("")
        }.joinToString("\n")

    companion object {
        fun read(input: List<String>): Map {
            val inputLines = input.takeWhile { it.trim() != "" }
            val coords = inputLines.map {
                val tmp = it.split(",")
                tmp[0].toInt() to tmp[1].toInt()
            }
            val nCols = coords.map { it.first }.maxOrNull()!! + 1
            val nRows = coords.map { it.second }.maxOrNull()!! + 1
            val tmpMap = (0 until nRows).map { BooleanArray(nCols) { false } }
            coords.forEach { (x, y) -> tmpMap[y][x] = true }
            return Map(nCols, nRows, tmpMap)
        }
    }

}

data class Fold(
    val alongX: Boolean,
    val at: Int
) {
    private fun foldX(map: Map): Map {
        val resNbCol = maxOf(at, map.nCols - at - 1)
        val nbPrefix = max(0, map.nCols - (2 * at + 1))
        val nbSuffix = max(0, at * 2 + 1 - map.nCols)
        val prefixLeft = BooleanArray(max(0, nbPrefix)) { false }
        val suffixRight = BooleanArray(max(0, nbSuffix)) { false }

        return Map(
            resNbCol,
            nRows = map.nRows,
            map.dots.map { row ->
                val left = prefixLeft.plus(row.take(at))
                val right = (row.drop(at).plus(suffixRight.toList())).reversed()
                left.zip(right).map { (a, b) -> a || b }.toBooleanArray()
            }
        )
    }

    private fun foldY(map: Map): Map {
        val rowsUp = map.dots.take(at)
        val rowsDown = map.dots.drop(at).reversed()

        return Map(
            map.nCols,
            map.nRows / 2,
            rowsUp.zip(rowsDown).map { (aa, bb) -> aa.zip(bb).map { (a, b) -> a || b }.toBooleanArray() }
        )
    }

    fun fold(map: Map) =
        if (alongX) {
            foldX(map)
        } else {
            foldY(map)
        }

    companion object {
        fun readFolds(input: List<String>): List<Fold> {
            val inputLines = input.dropWhile { it.trim() != "" }.drop(1)

            return inputLines.map { l ->
                val v = l.split("=")[1].toInt()
                if (l.startsWith("fold along x")) {
                    Fold(true, v)
                } else {
                    Fold(false, v)
                }
            }
        }
    }
}

fun main() {
    val input = inputLines("13", false)

    val map = Map.read(input)
    val folds = Fold.readFolds(input)

    println("nRows=${map.nRows} nCols=${map.nCols} count=${map.countDots()}")

    val foldedMap = folds.first().fold(map)
    println("nRows=${foldedMap.nRows} nCols=${foldedMap.nCols} count=${foldedMap.countDots()}")


    val n = foldedMap.countDots()
    println(n)

    val fm = folds.fold(map) { m, f -> f.fold(m) }
    println(fm)

}
