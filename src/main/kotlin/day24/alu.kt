package day24

import day24.Functions.Companion.parseDirects
import inputLines

fun checkIt(input: List<String>) {
    val ps = parseDirects(input)

    val model = Input(99999999999999L)

    ModelApplier.apply(model, ps)
}

fun part1(input: List<String>) {
    val ps = parseDirects(input)

    // cache , for (z, depth), the list of (z, w) that can reach it
    val cache = mutableMapOf<Pair<Int, Int>, List<Pair<Int, Int>>>()

    fun handler(zTarget: Int, remainFunctions: List<RegisterFunction>, models: List<String>, depth: Int): List<String> {
        if (remainFunctions.isEmpty()) {
            return models
        }

        fun computeWForZ():List<Pair<Int, Int>>{
            val f = remainFunctions.first()
            val zSrc = ModelApplier.investigate(f, zTarget)
            val zMaxWLoc = zSrc.groupBy { it.second }
                .map { (z, ps) -> z to ps.map { it.first }.maxOrNull()!! }
                .sortedBy { it.first }
            return zMaxWLoc
        }
        val zMaxW = cache.getOrPut(zTarget to depth){computeWForZ()}

        return zMaxW.fold(emptyList<String>()) { ms, (z, w) ->
            ms.plus(handler(z, remainFunctions.drop(1), models.map { "$w$it" }, depth + 1))
        }
    }

    val zeroModels = handler(0, ps.reversed(), listOf(""), 0)

    println("${zeroModels.minOrNull()} / ${zeroModels.maxOrNull()}")
}

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
fun main() {
    val input = inputLines("24", false)

    part1(input)
}
