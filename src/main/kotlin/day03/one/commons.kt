package day03.one

import kotlin.math.pow

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */

val nBit = 12
val filename = "src/main/kotlin/day03/diagnostic.txt"
//val nBit = 5
//val filename = "src/main/kotlin/day03/diagnostic-sample.txt"
val mask = (0 until nBit).toList().map { 2.toDouble().pow(it).toInt() }.reversed()
val totalMask = mask.sum()
