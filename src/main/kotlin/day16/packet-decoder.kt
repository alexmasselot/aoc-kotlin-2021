package day16

import inputLines
import splitIgnoreEmpty

sealed class Packet(
    version: Int,
    id: Int,
) {
    abstract fun countPackets(): Int
    abstract fun sumVersions(): Int
    abstract fun compute(): Long


    companion object {
        fun build(str: String): Packet =
            readNext(toBinaryString(str))!!.first

        fun readValue(bits: String): Pair<Long, String> {
            val nbDigits = bits.chunked(5)
                .takeWhile { it.startsWith("1") }
                .size + 1
            val digits = bits.chunked(5)
                .take(nbDigits)
                .map { it.drop(1) }

            val remain = bits.drop(5 * nbDigits)
            val value = digits.joinToString("").toLong(2)
            return value to remain
        }

        fun readNext(bits: String): Pair<Packet, String>? {
            if (bits.isEmpty()) {
                return null
            }
            val version = bits.take(3).toInt(2)
            val id = bits.drop(3).take(3).toInt(2)
            val afterVersionId = bits.drop(6)
            if (id == 4) {
                val (v, rem) = readValue(afterVersionId)
                return PacketValue(version, v) to rem
            } else {
                val lengthTypeId = afterVersionId.take(1)
                if (lengthTypeId == "0") {
                    val (packetLength, remain) = afterVersionId.drop(1).take(15).toInt(2) to afterVersionId.drop(16)
                    val packetBit = remain.take(packetLength)
                    val unparsed = remain.drop(packetLength)
                    fun handler(rem: String, acc: List<Packet>): List<Packet> {
                        val (packet, remain) = readNext(rem) ?: return acc
                        return handler(remain, acc.plus(packet))
                    }

                    val subPackets = handler(packetBit, emptyList())
                    return PacketOperator(version, id, subPackets) to unparsed
                } else {
                    val (packetCount, remain) = afterVersionId.drop(1).take(11).toInt(2) to afterVersionId.drop(12)
                    fun handler(i: Int, rem: String, acc: List<Packet>): Pair<List<Packet>, String> {
                        if (i == 0) {
                            return acc to rem
                        }
                        val (packet, remain) = readNext(rem)!!
                        return handler(i - 1, remain, acc.plus(packet))
                    }
                    val (subPackets, unparsed) = handler(packetCount, remain, emptyList())
                    return PacketOperator(version, id, subPackets) to unparsed
                }
            }
        }

    }
}

data class PacketValue(
    val version: Int,
    val value: Long
) : Packet(version, 4) {
    override fun countPackets() = 1
    override fun sumVersions() = version
    override fun compute() = value
}

val reduceFunctions = listOf<(xs: List<Long>) -> Long>(
    { xs: List<Long> -> xs.sum() },
    { xs: List<Long> ->
        xs.fold(1) { acc, x -> acc * x }
    },
    { xs: List<Long> -> xs.minOrNull()!! },
    { xs: List<Long> -> xs.maxOrNull()!! },
    { xs: List<Long> -> 0L },
    { xs: List<Long> -> if (xs[0] > xs[1]) 1L else 0L },
    { xs: List<Long> -> if (xs[0] < xs[1]) 1L else 0L },
    { xs: List<Long> -> if (xs[0] == xs[1]) 1L else 0L },

    )

data class PacketOperator(
    val version: Int,
    val id: Int,
    val subPackets: List<Packet>
) : Packet(version, id) {
    override fun countPackets() = subPackets.map { it.countPackets() }.sum()
    override fun sumVersions() = version + subPackets.map { it.sumVersions() }.sum()
    override fun compute() = reduceFunctions[id](subPackets.map { it.compute() })
}


fun toBinaryString(hexa: String): String =
    hexa.splitIgnoreEmpty("")
        .map { it.toInt(16) }
        .map {
            val x = it.toString(2)
            "0".repeat(4 - x.length) + x
        }
        .joinToString("")

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
fun main() {
    val input = inputLines("16", false)
    val packet = Packet.build(input.first())
    println(packet.sumVersions())
    println(packet.compute())
}
