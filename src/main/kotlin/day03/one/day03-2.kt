package day03.one

import java.io.File

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */

fun filterUntilUnique(iBit: Int, diags: List<Int>, isMost:Boolean): Int {
    val n = diags.size
    val n1 = diags.count { it and mask[iBit] != 0 }
    val match = if (n1 >= (n - n1)) {
        mask[iBit]
    } else {
        0
    }

    val filtered = diags.filter { (it and mask[iBit] == match) xor !isMost }

    if (filtered.size == 1) {
        return filtered.first()
    }
    return filterUntilUnique(iBit + 1, filtered, isMost)
}

fun main() {
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

    println("---------------------")
    val oxygenGeneratorRating =filterUntilUnique(0, diags, true)
    val co2ScrubberRating = filterUntilUnique(0, diags, false)
    println(oxygenGeneratorRating)
    println(co2ScrubberRating)
    println(oxygenGeneratorRating * co2ScrubberRating)

}
