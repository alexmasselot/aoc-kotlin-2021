package day07

import inputLines
import kotlin.math.abs

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */


fun main() {

    val input = inputLines("07", false)
        .first().split(",").map { it.toInt() }

    fun burn1 (x:Int, y:Int) = abs(x-y)
    fun burn2 (x:Int, y:Int) = (0..abs(x-y)).sum()

    val fuel: (Int) -> Int = { anchor ->
        input.map { burn2(it,anchor) }.sum()
    }


    val minFuel = (0..(input.maxOrNull()!!)).mapIndexed{ i, p -> i to fuel(p) }.minByOrNull{it.second}
    println(minFuel)

}
