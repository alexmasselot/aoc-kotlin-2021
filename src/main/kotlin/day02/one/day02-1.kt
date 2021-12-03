package day02.one

import java.io.File

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
data class Position(
    val horizontal: Int,
    val depth: Int
)

val transfoForward = { pos: Position, shift: Int -> pos.copy(horizontal = pos.horizontal + shift) }
val transfoDown = { pos: Position, shift: Int -> pos.copy(depth = pos.depth + shift) }
val transfoUp = { pos: Position, shift: Int -> pos.copy(depth = pos.depth - shift) }

val transfo = mapOf(
    "forward" to transfoForward,
    "down" to transfoDown,
    "up" to transfoUp
)

fun main() {
    val filename = "src/main/kotlin/day02/directions.txt"
    val orders = File(filename).readLines()
        .map { it.split(' ') }
        .map { (a, b) -> a to b.toInt() }

    val finalPosition = orders.fold(Position(0, 0)) { acc: Position, order: Pair<String, Int> ->
        transfo[order.first]!!(acc, order.second)
    }
    println(finalPosition)
    print(finalPosition.depth*finalPosition.horizontal)
}
