package day22

import inputLines
import java.util.*
import kotlin.math.max
import kotlin.math.min

class BrutalCube(val dim: Int) {
    private val width = 2 * dim + 1
    val bits = BitSet(width * width * width)

    private fun iCoords(x: Int, y: Int, z: Int) = (x + dim) + (y + dim) * width + (z + dim) * width * width

    fun countOn() = bits.cardinality()

    private fun range(w: Pair<Int, Int>) = (max(-dim, w.first)..min(dim, w.second))
    fun switch(sw: Switch) {
        range(sw.z).forEach { z ->
            range(sw.y).forEach { y ->
                val x1 = max(-dim, min(dim, sw.x.first))
                val x2 = max(-dim, min(dim, sw.x.second + 1))
                bits.set(iCoords(x1, y, z), iCoords(x2, y, z), sw.isOn)
            }
        }
    }
}

data class Switch(
    val x: Pair<Int, Int>,
    val y: Pair<Int, Int>,
    val z: Pair<Int, Int>,
    val isOn: Boolean
) {
    companion object {
        val re = """(\w+) x=(\-?\d+)\.\.(\-?\d+),y=(\-?\d+)\.\.(\-?\d+),z=(\-?\d+)\.\.(\-?\d+)""".toRegex()

        fun build(str: String): Switch {
            val (strOn, x1, x2, y1, y2, z1, z2) = re.matchEntire(str)!!.destructured
            return Switch(
                x1.toInt() to x2.toInt(),
                y1.toInt() to y2.toInt(),
                z1.toInt() to z2.toInt(),
                strOn == "on"
            )
        }
    }
}

fun part1(switches: List<Switch>) {
    val cube = BrutalCube(50)

    switches.forEach {
        cube.switch(it)
    }

    println(cube.countOn())
}

fun part2(switches: List<Switch>) {

    val cubes = switches.fold(emptyList<Cube>()) { cs, sw ->
        val cube =
            Cube(Coords(sw.x.first, sw.x.second), Coords(sw.y.first, sw.y.second), Coords(sw.z.first, sw.z.second))
        if (sw.isOn) {
            cs.flatMap { it.minus(cube) }.plus(cube)
        } else {
            cs.flatMap { it.minus(cube) }
        }
    }
    println("${cubes.size} cubes")
    println(cubes.sumOf { it.volume() })
}

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
fun main() {
    val input = inputLines("22", false)

    val switches = input.map { Switch.build(it) }

    part1(switches)

    part2(switches)

}
