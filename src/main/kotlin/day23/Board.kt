package day23

import kotlin.math.pow

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
    val isReachingHome = to < 8 && to / 2 == type

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
        if (from < 8 && from % 2 == 0 && from == to - 1) {
            return listOf(to)
        }
        /* hallway move */
        if (from >= 8 && to >= 8) {
            return ((from + 1)..to).toList()
        }

        val turnLeft = from / 2 + 9
        val turnRight = from / 2 + 10
        /* go to Hallway */
        if (to >= 8) {
            val turn = if (to <= turnLeft) turnLeft else turnRight
            val exit = if (from % 2 == 1) listOf(from - 1) else emptyList()
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

        val oversteps = setOf(
            0 to 9,
            0 to 10,
            9 to 10,
            2 to 10,
            2 to 11,
            10 to 11,
            4 to 11,
            4 to 12,
            11 to 12,
            6 to 12,
            6 to 13,
            12 to 13,
            9 to 0,
            10 to 0,
            10 to 9,
            10 to 2,
            11 to 2,
            11 to 10,
            11 to 4,
            12 to 4,
            12 to 11,
            12 to 6,
            13 to 6,
            13 to 12,
        )
    }
}


data class Board(val rep: Int, val isOccupied: Int) {

    val isDone = isOccupied == 0
    override fun toString(): String {
        val l = (0..14).map { i ->
            val amphs = rep.shr(2 * i) % 4
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
#${l[8]}${l[9]}.${l[10]}.${l[11]}.${l[12]}.${l[13]}${l[14]}#
###${l[0]}#${l[2]}#${l[4]}#${l[6]}###
  #${l[1]}#${l[3]}#${l[5]}#${l[7]}#
  #########"""
    }

    fun typeAt(pos: Int) = if (isOccupied.shr(pos) % 2 > 0) rep.shr(pos * 2) % 4 else null
    fun isOccupiedAt(pos: Int) = isOccupied.shr(pos) % 2 > 0
    fun remove(pos: Int) =
        Board(
            rep.and(3.shl(2 * pos).inv()),
            isOccupied.and(1.shl(pos).inv())
        )

    fun add(type: Int, pos: Int) =
        Board(
            rep.or(type.shl(2 * pos)),
            isOccupied.or(1.shl(pos))
        )

    fun isPossible(move: Move) =
        (isOccupied.and(move.bitMove) == 0) && typeAt(move.from) == move.type

    fun mutated(move: Move) =
        if (move.isReachingHome) {
            remove(move.from)
        } else {
            remove(move.from).add(move.type, move.to)
        }

    fun cleanUp() = (0..3).fold(this to 0) { acc, t ->
        if (typeAt(2 * t) == t && typeAt(2 * t + 1) == t) {
            acc.first.remove(2 * t).remove(2 * t + 1) to acc.second
        } else if (typeAt(2 * t + 1) == t) {
            acc.first.remove(2 * t + 1) to acc.second
        } else {
            acc.first to (acc.second + Move.typeCost(t))
        }
    }

    fun outMoves() = outMoves
        .filter { isPossible(it) }

    fun inMoves() = inMoves
        .filter { isPossible(it) }
        .filter { !isOccupiedAt(it.to + 1) }

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

            val trimmed = str.replace(reTrim, "")
            val chars = trimmed.toCharArray()
            val repAndFill = listOf(11, 15, 12, 16, 13, 17, 14, 18, 0, 1, 3, 5, 7, 9, 10).mapIndexed { i, strPos ->
                val locValue = when (chars[strPos]) {
                    'A' -> 0
                    'B' -> 1
                    'C' -> 2
                    'D' -> 3
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
//                // from not my alley to to the hallway
//                .plus(
            (0..3).flatMap { type ->
                (0..3).filter { it != type }.flatMap { alley ->
                    (8..14)
                        .filter { !((it < 8) && (it / 2 == type || it / 2 == alley)) }
                        .flatMap { i ->
                            listOf(Move(type, 2 * alley, i), Move(type, 2 * alley + 1, i))
                        }
                }
            }
//                )
                // get out from blocking my alley
                .plus(
                    (0..3).flatMap { type ->
                        (8..14).map { i -> Move(type, 2 * type, i) }
                    }
                )

        // from anywhere to my alley
        val inMoves =
            (0..3).flatMap { type ->
                (0..14)
                    .filter { it != type * 2 + 1 && it != 2 * type }
                    .map { i ->
                        Move(type, i, 2 * type)
                    }
            }

    }
}
