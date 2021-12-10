package day08

import day03.one.summarizeToInt
import day08.Signal.Companion.charPositions
import inputLines

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */

data class Explanation(val charPos: List<String>) {
    fun isComplete() = charPos.size == 7
}

class Signal(leftInput: List<String>, rightInput: List<String>) {
    val left = leftInput.map { it.split("").sorted().joinToString("") }.toSet()
    val right = rightInput.map { it.split("").sorted().joinToString("") }

    fun commons() =
        right.filter { keepLength.contains(it.length) }
            .filter { left.contains(it) }


    override fun toString() =
        "$left | $right"

    companion object {
        // length for 1, 7, 4, 8
        val keepLength = setOf(2, 3, 4, 7)

        // 0 to 9 position
        val charPositions = listOf(
            setOf(0, 1, 2, 4, 5, 6),
            setOf(2, 5),
            setOf(0, 2, 3, 4, 6),
            setOf(0, 2, 3, 5, 6),
            setOf(1, 2, 3, 5),
            setOf(0, 1, 3, 5, 6),
            setOf(0, 1, 3, 4, 5, 6),
            setOf(0, 2, 5),
            setOf(0, 1, 2, 3, 4, 5, 6),
            setOf(0, 1, 2, 3, 5, 6),
        )
    }
}

class Mapping(permutation: CharArray) {
     val string2Int =
        charPositions.mapIndexed { i, iVals ->
            iVals.map { permutation[it] }.sorted().joinToString("") to i
        }.toMap()

    fun map(str: String) = string2Int[str] ?: throw UnsupportedOperationException("no [$str] in $string2Int")

    fun decypher(input: List<String>) =
        input.fold(0) { acc, str -> 10 * acc + map(str) }

}

fun generatePermutations(values: Set<Char>): List<CharArray> {
    if (values.isEmpty()) {
        return emptyList()
    }

    fun handler(xs: Set<Char>): List<CharArray> {
        if (xs.size == 1) {
            return listOf(charArrayOf(xs.toList().first()))
        }
        return xs.flatMap { head ->
            handler(xs.minus(head))
                .map { charArrayOf(head).plus(it) }
        }
    }

    return handler(values)
}


fun checkPossible(signal: Signal, permutedChars: CharArray): Boolean {
    val numberRep = (0..9).map { i ->
        mapNumber(i, permutedChars)
    }.toSet()
    return signal.left.containsAll(numberRep)
}

fun mapNumber(i: Int, permutedChars: CharArray) = charPositions[i].map { permutedChars[it] }.sorted().joinToString("")

fun findExplanation(signal: Signal): CharArray {
    val charPermutations = generatePermutations(setOf('a', 'b', 'c', 'd', 'e', 'f', 'g'))
    return charPermutations.find {
        checkPossible(signal, it)
    } ?: throw UnsupportedOperationException("no explanation found")
}




fun part1(signals: List<Signal>) {
    val n = signals.map { it.commons().size }.sum()
    println(n)
}

fun part2(signals: List<Signal>) {
    val n = signals.map { signal ->
        val permutation = findExplanation(signal)
        val mapping = Mapping(permutation)
        mapping.decypher(signal.right)
    }.sum()
    println(n)
}

fun main() {

    val input = inputLines("08", false)
    val signals = input
        .map {
            val tmp = it.split(" | ")
            tmp[0] to tmp[1]
        }
        .map { (l, r) ->
            Signal(
                l.split(" "),
                r.split(" ")
            )
        }

    part2(signals)
}
