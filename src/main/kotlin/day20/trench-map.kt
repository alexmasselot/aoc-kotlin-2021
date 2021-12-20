package day20

import inputLines
import splitIgnoreEmpty
import utils.Matrix
import utils.ShiftDirection.*


data class Map(
    val pixels: Matrix<Int>,
    val background: Int = 0
) {
    fun count() = pixels.count { it == 1 }

    fun grow() = Map(pixels.expand(background), background)

    fun neighborsCode(): Matrix<Int> {
        val horiz = pixels.shift(LEFT, background)
            .zipN(
                pixels,
                pixels.shift(RIGHT, background)
            ).map { it -> combine(it, 2) }

        val vertList = horiz.shift(DOWN, background * 7)
            .zipN(
                horiz,
                horiz.shift(UP, background * 7)
            )

        return vertList.map { it -> combine(it, 8) }
    }

    fun applyAlgo(algo: IntArray): Map {
        return Map(
            grow().neighborsCode().map {
                algo[it]
            },
            background = algo[0] * (1 - background)
        )
    }

    override fun toString() =
        //"background: ${background} dimensions=${pixels.dimensions}\n"+
        pixels.values.map { r ->
            r.map { if (it == 1) "#" else "." }.joinToString("")
        }.joinToString("\n")

    companion object {
        fun combine(xs: List<Int>, factor: Int): Int {
//            if (xs.size != 3) {
//                throw ArrayIndexOutOfBoundsException("${xs.size}")
//            }
//            if (xs.any { it >= factor }) {
//                throw UnsupportedOperationException("$xs / $factor")
//            }
            return factor * factor * xs[0] + factor * xs[1] + xs[2]
        }
    }
}

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
object TrenchMap {
    fun decodeChar(s: String) = if (s == "#") 1 else 0

    fun build(input: List<String>): Pair<IntArray, Map> =
        input.first().splitIgnoreEmpty("").map { decodeChar(it) }.toIntArray() to
            Map(
                Matrix(
                    input.drop(2).map { row ->
                        row.splitIgnoreEmpty("").map { decodeChar(it) }
                    }
                ),
                0
            )
}

fun main() {
    val input = inputLines("20", false)

    val (algo, map) = TrenchMap.build(input)

    val res = (1..50).fold(map) { acc, i ->
        acc.applyAlgo(algo)
    }
    val n = res.count()
    println(n)

}
