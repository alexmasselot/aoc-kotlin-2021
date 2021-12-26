package day23

import kotlin.math.pow

const val ALLEY_LENGTH = 4
const val HALLWAY_START = ALLEY_LENGTH * 4

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
/*
################################
#  8  9 . 10 . 11 . 12 . 13 14 #
####### 0  # 2  # 4  # 6 #######
      # 1  # 3  # 5  # 7 #
      ####################
################################
# 16 17 . 18 . 19 . 20 . 21 22 #
####### 0  # 5  # 9  # 13 #######
      # 1  # 6  # 10 # 14 #
      # 2  # 7  # 11 # 15 #
      # 4  # 8  # 12 # 16 #
      #####################
      A -> 0
      B -> 1
      C -> 2
      D -> 3
 */
data class Move(
    val type: Int,
    val from: Int,
    val to: Int
) {
    val steps = computeSteps()
    val isReachingHome = to < HALLWAY_START && to / ALLEY_LENGTH == type

    val bitMove = steps.fold(0) { acc, i -> acc + 1.shl(i) }

    val cost: Int by lazy {
        typeCost(type) * (steps.size + oversteps.intersect(listOf(from).plus(steps).zipWithNext()).size)
    }

    private fun computeSteps(): List<Int> {
        if (from == to) {
            return emptyList()
        }
        if (from > to) {
            return Move(type, to, from).steps.reversed().plus(to).minus(from)
        }
        /* same alley*/
        if (from < HALLWAY_START && from / ALLEY_LENGTH == to / ALLEY_LENGTH) {
            return listOf(to)
        }
        /* hallway move */
        if (from >= HALLWAY_START && to >= HALLWAY_START) {
            return ((from + 1)..to).toList()
        }

        val turnLeft = from / ALLEY_LENGTH + HALLWAY_START + 1
        val turnRight = from / ALLEY_LENGTH + HALLWAY_START + 2
        /* go to Hallway */
        if (to >= HALLWAY_START) {
            val turn = if (to <= turnLeft) turnLeft else turnRight
            val alleyExit = (from / ALLEY_LENGTH) * ALLEY_LENGTH
            val exit = if (from == alleyExit) emptyList() else (alleyExit..(from - 1)).reversed()
            return exit.plus(turn).plus(Move(type, turn, to).steps)
        }
        /* go through hallway to climb up in another alley */
        val turn = if (to <= from) turnLeft else turnRight
        return Move(type, from, turn).steps.plus(Move(type, turn, to).steps)
    }

    override fun toString() =
        "${(if (type == 15) '*' else 'A'.plus(type))}: $from -> $to"

    companion object {
        fun typeCost(type: Int) = (10.0).pow(type).toInt()


        val oversteps =
            (0..3).flatMap { i ->
                listOf(
                    ALLEY_LENGTH * i to i + HALLWAY_START + 1,
                    ALLEY_LENGTH * i to i + HALLWAY_START + 2,
                    i + HALLWAY_START + 1 to ALLEY_LENGTH * i,
                    i + HALLWAY_START + 2 to ALLEY_LENGTH * i,
                    i + HALLWAY_START + 1 to i + HALLWAY_START + 2,
                    i + HALLWAY_START + 2 to i + HALLWAY_START + 1,
                )
            }.toSet()
    }
}


data class Board(val rep: Long, val isOccupied: Int) {

    fun signature() = "$rep/${isOccupied.toString(2)}"

    val isDone = isOccupied == 0
    override fun toString(): String {
        val l = (0..(HALLWAY_START + 6)).map { i ->
            val amphs = (rep.shr(2 * i) % 4).toInt()
            val isOcc = isOccupied.shr(i) % 2
            if (isOcc == 1) {
                when (amphs) {
                    0 -> 'A'
                    1 -> 'B'
                    2 -> 'C'
                    3 -> 'D'
                    else -> '#'
                }
            } else {
                '.'
            }
        }
        return """#############
#${l[HALLWAY_START]}${l[HALLWAY_START + 1]}.${l[HALLWAY_START + 2]}.${l[HALLWAY_START + 3]}.${l[HALLWAY_START + 4]}.${l[HALLWAY_START + 5]}${l[HALLWAY_START + 6]}#
""" +
            (0 until ALLEY_LENGTH).map { i ->
                (if (i == 0) "##" else "  ") +
                    "#${l[0 + i]}#${l[ALLEY_LENGTH + i]}#${l[2 * ALLEY_LENGTH + i]}#${l[3 * ALLEY_LENGTH + i]}#" +
                    (if (i == 0) "##" else "")
            }.joinToString("\n") +
            "\n  #########"
    }

    fun typeAt(pos: Int) = if (isOccupied.shr(pos) % 2 > 0) (rep.shr(pos * 2) % 4).toInt() else null
    fun isOccupiedAt(pos: Int) = isOccupied.shr(pos) % 2 > 0
    fun isAnyOccupiedAt(pos: IntRange) = pos.any { isOccupiedAt(it) }

    fun remove(pos: Int) =
        Board(
            rep.and(3L.shl(2 * pos).inv()),
            isOccupied.and(1.shl(pos).inv())
        )

    fun add(type: Int, pos: Int) =
        Board(
            rep.or(type.toLong().shl(2 * pos)),
            isOccupied.or(1.shl(pos))
        )

    fun isPossible(move: Move): Boolean {
        //println("$move ${typeAt(move.from) == move.type}\n${isOccupied.toString(2)}\n${move.bitMove.toString(2)}\n${move.steps}")
        return (isOccupied.and(move.bitMove) == 0) && typeAt(move.from) == move.type
    }


    fun mutated(move: Move) =
        if (move.isReachingHome) {
            remove(move.from)
        } else {
            remove(move.from).add(move.type, move.to)
        }

    fun cleanUp() = (0..3).fold(this to 0) { acc, t ->
        val exit = t * ALLEY_LENGTH
        val bottom = exit + ALLEY_LENGTH - 1
        val toRemove = (exit..bottom).reversed().takeWhile { typeAt(it) == t }
        val score = (1 until (ALLEY_LENGTH - toRemove.size)).sum() * Move.typeCost(t)
        toRemove.fold(acc.first) { a, i -> a.remove(i) } to acc.second + score
    }

    fun outMoves() = outMoves
        .filter { isPossible(it) }

    fun inMoves() = inMoves
        .filter { isPossible(it) }
        .filter { !isAnyOccupiedAt((it.to + 1) until it.to + ALLEY_LENGTH) }

    companion object {
        /*
        #############
        #...........#
        ###B#C#B#D###
          #A#D#C#A#
          #########
         */
        val reTrim = """[\n# ]""".toRegex()
        fun fromString(str: String): Board {

            val str4 = if (ALLEY_LENGTH == 4) {
                val ls = str.split("\n")
                ls.take(3).plus(listOf("  #D#C#B#A#", "  #D#B#A#C#")).plus(ls.drop(3)).joinToString("\n")
            } else {
                str
            }

            val trimmed = str4.replace(reTrim, "")
            val chars = trimmed.toCharArray()
            val repAndFill =
                emptyList<Int>()
                    .plus(
                        (0 until 4)
                            .flatMap { a ->
                                (0 until ALLEY_LENGTH).map { d ->
                                    11 + d * 4 + a
                                }
                            })
                    .plus(listOf(0, 1, 3, 5, 7, 9, 10)).mapIndexed { i, strPos ->
                        val locValue = when (chars[strPos]) {
                            'A' -> 0L
                            'B' -> 1L
                            'C' -> 2L
                            'D' -> 3L
                            else -> 0
                        }
                        locValue.shl(2 * i) to (chars[strPos] != '.')
                    }
            var rep = repAndFill.map { it.first }.sum()
            val isOccupied = repAndFill.map { it.second }.mapIndexed { i, b -> if (b) 1.shl(i) else 0 }.sum()
            return Board(rep, isOccupied)
        }


        /**
         * Moves any:
         * -> 8, 9, 10, 11, 12, 13, 14
        useless moves 8->9, 13->14
        A : *\1 -> 0
        B : *\3 -> 2
        C : *\5 -> 4
        D : *\7 -> 6
        automatic 0->1, 2->3, 4->5, 6->7

        Path i->j (i<j)
        i<8 & i%2==0 => i+1
        i<8 & i%2==0 => i/2+9, i/2+10
        i>=8 & i<14 => i+1
         */


        val outMoves: List<Move> =
//            // hallway rightward
//            (0..3).flatMap { type ->
//                (8..14).flatMap { i ->
//                    (8 until i)
//                        .filter { j -> !((j == 8 || j == 13) && (i - j) == 1) }
//                        .map { j -> Move(type, j, i) }
//                }
//            }
//                // hallway leftward
//                .plus(
//                    (0..3).flatMap { type ->
//                        (8..14).flatMap { i ->
//                            (8 until i)
//                                .filter { j -> !((j == 8 || j == 13) && (i - j) == 1) }
//                                .map { j -> Move(type, i, j) }
//                        }
//                    })
//                // from not my alley to the hallway
//                .plus(
            (0..3).flatMap { type ->
                (0..3).filter { it != type }.flatMap { alley ->
                    (HALLWAY_START..(HALLWAY_START + 6))
                        .filter { !((it / ALLEY_LENGTH == type) || (it / ALLEY_LENGTH == alley)) }
                        .flatMap { i ->
                            (0 until ALLEY_LENGTH).map { Move(type, ALLEY_LENGTH * alley + it, i) }
                        }
                }
            }
//                )
                // get out from blocking my alley
                .plus(
                    (0..3).flatMap { type ->
                        (0 until ALLEY_LENGTH - 1).flatMap { d ->
                            (HALLWAY_START..(HALLWAY_START + 6)).map { i -> Move(type, ALLEY_LENGTH * type + d, i) }
                        }
                    }
                )

        // from anywhere to my alley
        val inMoves =
            (0..3).flatMap { type ->
                (0..HALLWAY_START + 8)
                    .filter { !(it < HALLWAY_START && it / ALLEY_LENGTH == type) }
                    .map { i ->
                        Move(type, i, ALLEY_LENGTH * type)
                    }
            }

    }
}
