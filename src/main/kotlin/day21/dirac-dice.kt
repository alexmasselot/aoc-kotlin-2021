package day21

import inputLines
import utils.DAG
import java.lang.Long.max

const val DICE_MAX = 100

data class Dice(
    val points: Int = 0,
    val nbThrow: Int = 0
) {
    fun play() = Dice(points % DICE_MAX + 1, nbThrow + 1)

    fun play(n: Int): Pair<Dice, Int> {
        fun handler(n: Int, acc: Int, d: Dice): Pair<Dice, Int> {
            if (n == 0) {
                return d to acc
            }
            val d2 = d.play()
            return handler(n - 1, acc + d2.points, d2)
        }
        return handler(n, 0, this)
    }

    override fun toString() = "$points (x$nbThrow)"
}

data class Player(
    val position: Int,
    val score: Int = 0,
) {
    fun move(forward: Int): Player {
        val p = (position + forward) % 10
        return Player(if (p == 0) 10 else p, score)
    }

    fun scoreUp() = Player(position, score + position)

    fun moveAndScore(forward: Int) = move(forward).scoreUp()

    fun hasWon(limit: Int) = score >= limit

    override fun toString() = "$score @$position"

}

data class PlayerPairDice(val player1: Player, val player2: Player, val dice: Dice) {
    fun play(): PlayerPairDice {
        val (newDice, points) = dice.play(3)
        return PlayerPairDice(player2, player1.move(points).scoreUp(), newDice)
    }


    override fun toString() = "$player1, $player2, $dice"

}

fun playQuantum(player: Player) =
    (3..9).map { player.moveAndScore(it) }

data class PlayerPair(val player1: Player, val player2: Player, val is1ToGo: Boolean) {

    fun playQuantum() =
        if (is1ToGo) playQuantum(player1).map {
            copy(
                player1 = it,
                is1ToGo = !is1ToGo
            )
        } else playQuantum(player2).map { copy(player2 = it, is1ToGo = !is1ToGo) }

    fun hasWon(limit: Int) = player1.hasWon(limit) || player2.hasWon(limit)

    override fun toString() = "($player1, $player2)"
}

object PlayerPlayInt {
    fun starter(position1: Int, position2: Int) = position2Int(position1 - 1, 0, position2 - 1, 0, true)

    fun position2Int(pos1: Int, score1: Int, pos2: Int, score2: Int, is1ToGo: Boolean) =
        (if (is1ToGo) 0 else 1) + pos1 * 2 + pos2 * 2 * 10 + score1 * 2 * 10 * 10 + score2 * 2 * 10 * 10 * 21

    val largestPosition = position2Int(9, 20, 9, 20, false)

    fun toString(players: Int): String {
        val is1ToGo = (players % 2) == 0
        val position1 = (players / 2) % 10
        val position2 = (players / (2 * 10)) % 10
        val score1 = (players / (2 * 10 * 10)) % 21
        val score2 = (players / (2 * 10 * 10 * 21)) % 21
        return "$score1 @${position1 + 1} x $score2 @${position2 + 1} toGo=${if (is1ToGo) 1 else 2}"
    }

    val diceRolls = (1..3).flatMap { d1 -> (1..3).flatMap { d2 -> (1..3).map { d3 -> d1 + d2 + d3 } } }
    fun next(players: Int): List<Int> {
        val is1ToGo = (players % 2) == 0
        val position1 = (players / 2) % 10
        val position2 = (players / (2 * 10)) % 10
        val score1 = (players / (2 * 10 * 10)) % 21
        val score2 = (players / (2 * 10 * 10 * 21)) % 21


        return if (is1ToGo) {
            diceRolls.map { add ->
                val newPosition = (position1 + add) % 10
                val newScore = score1 + newPosition + 1
                if (newScore >= 21) {
                    -1
                } else {
                    position2Int(newPosition, newScore, position2, score2, !is1ToGo)
                }
            }
        } else {
            diceRolls.map { add ->
                val newPosition = (position2 + add) % 10
                val newScore = score2 + newPosition + 1
                if (newScore >= 21) {
                    -2
                } else {
                    position2Int(position1, score1, newPosition, newScore, !is1ToGo)
                }
            }
        }
    }

    val transitions: Array<List<Int>> by lazy {
        (0..largestPosition).map { players -> next(players) }.toTypedArray()
    }

    fun computeWinners(pos1: Int, pos2: Int): Pair<Long, Long> {
        var winner1 = 0L
        var winner2 = 0L

        val root = starter(pos1, pos2)
        val counts = Array(transitions.size) { 0L }
        counts[root] = 1L
        transitions.forEachIndexed { i, moves ->
            val c = counts[i]
            moves.forEach { j ->
                when (j) {
                    -1 -> winner1 += c
                    -2 -> winner2 += c
                    else -> counts[j] += c
                }
            }
        }

        return winner1 to winner2
    }
}

fun buildPlayMultiverse(root: PlayerPair, limit: Int): DAG<PlayerPair> {
    fun handler(acc: DAG<PlayerPair>, toPlay: List<PlayerPair>): DAG<PlayerPair> {
        if (toPlay.isEmpty()) {
            return acc
        }
        val moves = toPlay.flatMap { n -> n.playQuantum().map { n to it } }

        val notYetPlayed =
            moves.filter { (_, to) -> !acc.contains(to) && !to.hasWon(limit) }
                .map { it.second }
        println("${notYetPlayed.size}/${moves.size}/${toPlay.size}/${acc.countNodes()}")
        val newGraph = acc.plus(moves)

        return handler(newGraph, notYetPlayed)
    }

    return handler(DAG(), listOf(root))
}

fun part1(pos1: Int, pos2: Int): PlayerPairDice {
    val players = PlayerPairDice(Player(pos1), Player(pos2), Dice(0))
    fun handler(ps: PlayerPairDice): PlayerPairDice {
        println(ps)
        if (ps.player2.hasWon(21)) {
            return ps
        }
        return handler(ps.play())
    }
    return handler(players)
}

// this version is kind of doomed, as it uses the too generic DAG
fun part2Dumb(pos1: Int, pos2: Int) {
    val root = PlayerPair(Player(pos1), Player(pos2), true)

    val graph = buildPlayMultiverse(root, 21)
    println("--------")
    println("${graph.countNodes()} nodes & ${graph.countEdges()} edges")
    val countPaths = graph.countPaths(root)
    println(countPaths.values.sum())
}

fun part2(pos1: Int, pos2: Int) {

    val wins = PlayerPlayInt.computeWinners(pos1, pos2)

    println(wins)
    println(max(wins.first, wins.second))
}

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
fun main() {
    val input = inputLines("21", true)

    //val players = part1(4, 8)
//    val players = part1(10, 4)
    // println(" => $players")
    //println(players.player1.score * players.dice.nbThrow)

    part2(4, 8)
    part2(10, 4)
}
