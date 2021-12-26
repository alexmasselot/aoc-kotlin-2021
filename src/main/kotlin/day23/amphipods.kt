package day23

import day23.Board.Companion.inMoves
import day23.Board.Companion.outMoves
import inputLines


val debug = listOf(
    """#############
#...........#
###B#C#B#D###
  #.#D#.#A#
  #########""".trimIndent(),
    """#############
#...B.......#
###B#C#.#D###
  #.#D#.#A#
  #########""".trimIndent(),
    """#############
#...B.......#
###B#.#.#D###
  #.#D#.#A#
  #########""".trimIndent(),
    """#############
#.....D.....#
###B#.#.#D###
  #.#.#.#A#
  #########""".trimIndent(),
    """#############
#.....D.....#
###.#.#.#D###
  #.#.#.#A#
  #########""".trimIndent(),
    """#############
#.....D.D.A.#
###.#.#.#.###
  #.#.#.#.#
  #########""".trimIndent(),
    """#############
#.........A.#
###.#B#C#.###
  #A#B#C#.#
  #########""".trimIndent(),
    """#############
#...........#
###.#.#.#.###
  #.#.#.#.#
  #########""".trimIndent()
).map { Board.fromString(it) }.toSet()

fun explore(board: Board) {


    val board2Cost = mutableMapOf<Board, Int>()
    var minCost = Int.MAX_VALUE

    val stack = ArrayDeque<Pair<Board, Int>>()
    stack.add(board.cleanUp())

    var i = 0
    do {
        i += 1
        val b = stack.removeFirst()
        if (debug.contains(b.first)) {
            println(b)
        }
        if (board2Cost[b.first]?.let { it < b.second } == true) {
            continue
        }
        val nextOut = b.first.outMoves()
            .map { b.first.mutated(it) to (it.cost + b.second) }
            // keep new aor with better scores than current or a cost higher that the current minimum winning cost
            .filter { sb -> sb.second < minCost && board2Cost[sb.first]?.let { sb.second < it } ?: true }

        nextOut.forEach { board2Cost[it.first] = it.second }

        val nextIn = b.first.inMoves()
            .map { b.first.mutated(it) to (it.cost + b.second) }

        val (nextInWin, nextInGo)= nextIn.partition { it.first.isDone }
        println("---------------------------------")
        println(b.first)
//        nextOut.forEach { println(it.first) }
//        println("===================")
//        nextIn.forEach { println(it.first) }


        nextInWin.forEach {
            println("WINNER ${it.second}")

            if (minCost > it.second) {
                minCost = it.second
            }
        }
        nextOut.plus(nextInGo).forEach { stack.addLast(it) }

    } while (stack.isNotEmpty())

    println("min cost = $minCost")

//    tailrec fun handler(b: Board, cumCost: Int) {
//        val next = allMoves
//            .filter { it.isPossible(board) }
//            .map { b.mutated(it) to (it.cost() + cumCost) }
//            // keep new aor with better scores or a cost higher that the minimum winning cost
//            .filter { sb -> sb.second < minCost && board2Cost[sb.first]?.let { sb.second < it } ?: true }
//
//        val (wins, toProcess) = next.partition { it.first.isDone }
//        wins.forEach {
//            println("WINNER ${it.second}")
//
//            if (minCost > it.second) {
//                minCost = it.second
//            }
//        }
//        toProcess.forEach { handler(it.first, it.second) }
//    }
//    handler(board.cleanUp(), 0)
}

fun part1(board: Board) {
    explore(board)
}

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
fun main() {
    val input = inputLines("23", false)

    val board = Board.fromString(input.joinToString("\n"))
    println(board)
    part1(board)
}
