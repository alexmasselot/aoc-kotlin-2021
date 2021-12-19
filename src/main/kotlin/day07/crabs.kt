package day07

import inputLines
import kotlin.math.abs

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */


// find the minimum in a quadratic  function
fun minDichotQuadratic(p0: Pair<Int, Int>, p1: Pair<Int, Int>, f: (Int) -> Int): Pair<Int, Int> {
    val (x0, res0) = p0
    val (x1, res1) = p1
    if (x0 == x1) {
        return p0
    }
    // take the pair with the minimum second member.
    // takes the second one if second values are identical
    fun minFromPair(p0: Pair<Int, Int>, p1: Pair<Int, Int>) = if (p0.second == p1.second) {
        p1
    } else if (p0.second < p1.second) {
        p0
    } else {
        p1
    }

    if (x1 == x0 + 1) {
        return minFromPair(p0, p1)
    }
    val x = (x0 + x1) / 2
    val res = f(x)
    if (f(x + 1) == f(x - 1)) {
        // it's locally flat. frankly, on which side shall we glide ?
        // we reduce by 1 or 2 the interval
        val min0 = minFromPair(p0, p0.first + 1 to f(p0.first + 1))
        val min1 = minFromPair(p1, p1.first - 1 to f(p1.first - 1))
        return minDichotQuadratic(min0, min1, f)
    }
    if (f(x - 1) < f(x + 1)) {
        // glide on  left
        return minDichotQuadratic(p0, x to res, f)
    }
    // glide on right
    return minDichotQuadratic(x to res, p1, f)
}

fun main() {

    val input = inputLines("07", false)
        .first().split(",").map { it.toInt() }

    fun burn1(x: Int, y: Int) = abs(x - y)
    fun burn2(x: Int, y: Int) = abs(x - y) * (abs(x - y) + 1) / 2

    val fuel: (Int) -> Int = { anchor ->
        input.map { burn2(it, anchor) }.sum()
    }


    val min = input.minOrNull()!!
    val max = input.maxOrNull()!!
    val minFuel = minDichotQuadratic(
        min to fuel(min),
        max to fuel(max),
        fuel
    )
    // the brutal linear version
    // 0..(input.maxOrNull()!!)).mapIndexed { i, p -> i to fuel(p) }.minByOrNull { it.second }
    println(minFuel)

}
