package day03.one

import java.io.File
import kotlin.math.pow

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
val nBit = 12;
val mask = 0.rangeTo(nBit - 1).toList().map { 2.toDouble().pow(it).toInt() }.reversed()

fun add(counter: List<Int>, diag: Int): List<Int> {
    return mask.map {
        if (diag and it > 0) {
            1
        } else {
            -1
        }
    }
        .zip(counter)
        .map { (plus, acc) -> acc + plus }
}

// Build the final integer, based on wether the counter is positive (bit = 1) or negative (bit =0)
fun summarizeToInt(counter: List<Int>): Int {
    if (counter.any { it == 0 }) {
        throw UnsupportedOperationException("tie counter in $counter")
    }
    return mask.zip(counter).map {
        if (it.second > 0) {
            it.first
        } else {
            0
        }
    }.sum()
}

fun main() {

    val filename = "src/main/kotlin/day03/diagnostic.txt"
    val diags = File(filename).readLines()
        .map { it.toInt(2) }

    val initCounter = mask.map { 0 }

    val finalCounter = diags.fold(initCounter) { acc, diag ->
        add(acc, diag)
    }

    println(finalCounter)
    val sumCounter = summarizeToInt(finalCounter)
    val totalMask = mask.sum()
    val invertSumCounter = totalMask xor sumCounter
    println(sumCounter)
    println(invertSumCounter)
    print(sumCounter * invertSumCounter)


}
