package day05

import java.io.File

data class Line(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int
) {
    fun isHorizontal() = y1 == y2
    fun isVertical() = x1 == x2
}

class Map(val width: Int) {
    var grid = Array(width) {
        IntArray(width) { 0 }
    }

    fun sortedRange(a: Int, b: Int) =
        if (a <= b) {
            a..b
        } else {
            a downTo b
        }

    fun addLine(l: Line) {
        if (l.isVertical()) {
            sortedRange(l.y1, l.y2).forEach { grid[it][l.x1] += 1 }
        } else
            if (l.isHorizontal()) {
                sortedRange(l.x1, l.x2).forEach { grid[l.y1][it] += 1 }
            } else {
                sortedRange(l.y1, l.y2).zip(sortedRange(l.x1, l.x2))
                    .forEach { (y, x) -> grid[y][x] += 1 }
            }
    }

    fun countDanger() =
        grid.map { line -> line.count { it >= 2 } }.sum()


    override fun toString() = grid
        .map { line ->
            line.map {
                if (it == 0) {
                    "."
                } else {
                    it
                }
            }.joinToString(" ")
        }
        .joinToString("\n")


}

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
fun main() {
    val tag = "" //""-sample"
    val filename = "src/main/kotlin/day05/lines$tag.txt"

    val lines = File(filename)
        .readLines()
        .mapNotNull {
            """(\d+),(\d+) \-> (\d+),(\d+)""".toRegex().find(it)
        }
        .map {
            val (x1, y1, x2, y2) = it.destructured
            Line(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
        }

    val width = lines.flatMap { listOf(it.x1, it.y1, it.x2, it.y2) }
        .maxOrNull() ?: 0

    val map = Map(width + 1)
    lines.forEach { map.addLine(it) }

    println(map)
    println(map.countDanger())
}
