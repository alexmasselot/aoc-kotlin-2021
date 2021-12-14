package day14

import inputLines

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */

data class MolPair(val pairCount: Map<String, Long>, val endsWith: Char) {

    fun countChars() =
        pairCount.map { (ab, n) ->
            ab[0] to n
        }.plus(endsWith to 1L)
            .groupBy { it.first }
            .map { (c, cs) -> c to cs.sumOf { it.second } }
            .toMap()

    fun deltaChar(): Long {
        val counts = countChars().values
        return counts.maxOrNull()!! - counts.minOrNull()!!
    }

    companion object {
        fun build(template: String) =
            MolPair(
                template
                    .split("")
                    .filter { it != "" }.zipWithNext()
                    .map { (a, b) -> "$a$b" }
                    .groupBy { it }
                    .map { (x, xs) -> x to xs.size.toLong() }
                    .toMap(),
                template.last()
            )

    }

}

class Polymer(transfoList: List<Pair<String, String>>) {
    private val insertions = transfoList.toMap()

    private val charIndex = insertions.keys
        .flatMap { it.split("") }
        .filter { it != "" }
        .distinct()
        .sorted()
        .mapIndexed { i, c -> c to i }

    fun check() {
        val n = insertions.keys.flatMap { it.split("") }.filter { it != "" }.distinct().size
        println("nChar=$n transfo = ${insertions.size}")
    }

    fun process(from: String): String {
        return from.split("").zipWithNext()
            .map { (a, b) ->
                insertions["$a$b"]?.let { "$a$it" } ?: "$a"
            }
            .joinToString("")
    }

    fun process(molPair: MolPair): MolPair =
        MolPair(
            molPair.pairCount.flatMap { (ab, n) ->
                val a = ab[0]
                val b = ab[1]
                val c = insertions[ab]!!
                listOf("$a$c" to n, "$c$b" to n)
            }
                .groupBy { it.first }
                .map { (ab, ns) -> ab to ns.map { it.second }.sum() }
                .toMap(),
            molPair.endsWith
        )

    fun process(molPair: MolPair, nTimes: Int): MolPair =
        (1..nTimes).fold(molPair) { mp, i -> process(mp) }

    companion object {
        private val reRead = """(\w\w) \-> (\w)""".toRegex()
        fun read(input: List<String>) =
            Polymer(input
                .mapNotNull { reRead.matchEntire(it) }
                .map { it.destructured }
                .map { (a, b) -> a to b }
            )
    }

}

fun main() {
    val input = inputLines("14", false)

    val molPair = MolPair.build(input.first().trim())
    val polymer = Polymer.read(input.drop(2))
    polymer.check()

    println(polymer.process(molPair, 40).deltaChar())
}
