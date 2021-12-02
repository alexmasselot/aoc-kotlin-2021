package day01

import java.io.File

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
fun main() {
    val filename = "src/day01/depths.txt"
    val n = File(filename).readLines()
        .map{it.split(' ').first().toInt()}
        .windowed(3)
        .map{it.sum()}
        .zipWithNext()
        .filter { (a, b) -> a < b }
        .count()

    println(n)
}
