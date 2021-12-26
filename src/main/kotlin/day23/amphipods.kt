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

val debugSignatures = setOf(
    "52777095387709/10000011100011111110111",
    "57175141833277/11000011100011011110111",
    "58274653198909/11100011100010011110111",
    "58274653198909/11100111100000011110111",
    "",
    "",
)

fun explore(board: Board) {
    val board2Cost = mutableMapOf<Board, Int>()
    var minCost = Int.MAX_VALUE

    val stack = ArrayDeque<Pair<Board, Int>>()
    stack.add(board.cleanUp())
    println("out moves")
    println(outMoves.joinToString("\n"))
    println("in moves")
    println(inMoves.joinToString("\n"))

    var i = 0
    do {
        i += 1
        val b = stack.removeFirst()
//        println(b)
//        println(b.first.rep)
//        println(b.first.isOccupied.toString(2))

//        if (debug.contains(b.first)) {
//            println(b)
//        }
        if (board2Cost[b.first]?.let { it < b.second } == true) {
            continue
        }

        if (debugSignatures.contains(b.first.signature())) {

            println("========================================= DEBUG =====================================================")
            println(b.first)
            println(b.first.outMoves())
            println(b.first.signature())
            println(">>>>>>>>>>>>>>")
            b.first.outMoves().forEach {
                println("${b.first} \n $it \n${b.first.mutated(it)}\n${b.first.mutated(it).signature()}")
                println(it.steps)
                println("isReaching home ${it.isReachingHome}")

            }
            println("<<<<<<<<<<<<<<")
            b.first.inMoves().forEach {
                println("${b.first} \n $it \n${b.first.mutated(it)}\n${b.first.mutated(it).signature()}")
                println(it.steps)
                println("isReaching home ${it.isReachingHome}")
            }
        }
        val nextOut = b.first.outMoves()
            .map { b.first.mutated(it) to (it.cost + b.second) }
            // keep new aor with better scores than current or a cost higher that the current minimum winning cost
            .filter { sb -> sb.second < minCost && board2Cost[sb.first]?.let { sb.second < it } ?: true }

        nextOut.forEach { board2Cost[it.first] = it.second }

        val nextIn = b.first.inMoves()
            .map { b.first.mutated(it) to (it.cost + b.second) }

        val (nextInWin, nextInGo) = nextIn.partition { it.first.isDone }
//        nextOut.forEach { println(it.first) }
//        println("===================")
//        nextIn.forEach { println(it.first) }


        nextInWin.forEach {

            if (minCost > it.second) {
                println("WINNER ${it.second}")
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
