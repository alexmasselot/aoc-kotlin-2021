package day10

import inputLines
import java.util.*

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */

val opening = mapOf(
    '}' to '{',
    ')' to '(',
    ']' to '[',
    '>' to '<'
)
val closing = opening.entries.map { it.value to it.key }.toMap()

val closingCharCorruptedScore = mapOf(
    '}' to 1197,
    ')' to 3,
    ']' to 57,
    '>' to 25137
)

val closingCharMissingScore = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4
)

fun corruptedChar(line: String): Char? {
    val queue = ArrayDeque<Char>()
    line.toCharArray().forEach { c ->
        opening[c]?.let { openingChar ->
            if (queue.isEmpty()) {
                return c
            }
            if (openingChar == queue.first()) {
                queue.pop()
            } else {
                return c
            }
        } ?: queue.push(c)
    }
    return null
}


// let's assume the line passed is not corrupted
fun completeLine(line: String): String {
    val re = """.*(\(\)|\{\}|\[\]|<>).*""".toRegex()

    fun eat(str: String): String {
        return re.matchEntire(str)?.let { matcher ->
            val (x) = matcher.destructured
            eat(str.replace(x, ""))
        } ?: str
    }

    val unmatched = eat(line)
    return unmatched.toCharArray().reversed().map { closing[it] }.joinToString("")
}

fun scoreMissing(str: String): Long {
    return str.toCharArray().fold(0L) { acc, c -> acc * 5 + (closingCharMissingScore[c] ?: 0) }
}


fun main() {
    val input = inputLines("10", false)


    val scoreCorrupted = input.map { corruptedChar(it) }
        .filterNotNull()
        .map { closingCharCorruptedScore[it]!! }
        .sum()

    println("score corrupted = $scoreCorrupted")

    val scoreMissings = input.filter { corruptedChar(it) == null }

        .map {
            completeLine(it)
        }
        .map { it to scoreMissing(it) }
        .filter { it.second != 0L }
        .map{it.second}

    println(scoreMissings.sorted()[scoreMissings.size/2])
}
