package day17

import inputLines
import kotlin.math.min
import kotlin.math.sign
import kotlin.math.sqrt


data class Velocity(
    val vx: Int,
    val vy: Int
) {
    fun increment() =
        Velocity(
            vx - 1 * sign(vx.toDouble()).toInt(),
            vy - 1
        )

    fun yForT(t: Int) = (2 * t * vy + t - t * t) / 2

    val xMax = vx * (vx + 1) / 2
    fun xForT(t: Int) = xMax - (vx - min(t, vx)) * ((vx - min(t, vx)) + 1) / 2

    fun tForY(y: Int) = ((2.0 * vy + 1) + sqrt((2.0 * vy + 1) * (2 * vy + 1) - 8 * y)) / 2.0

    fun tMatchY(yRange: IntRange): List<Int> {
        val t0 = tForY(yRange.last)
        val t1 = tForY(yRange.first)
        val n0 = if (t0.rem(1.0) == 0.0) t0.toInt() else t0.toInt() + 1
        val n1 = if (t1.rem(1.0) == 0.0) t1.toInt() else t1.toInt()
        return (n0..n1).toList()
    }

    fun matchingXAt(t: Int, xRange: IntRange) = xRange.contains(xForT(t))

    fun reachY(yRange: IntRange): Boolean {
        val t0 = tForY(yRange.last)
        val t1 = tForY(yRange.first)
        return (t0.rem(1.0) == 0.0) || ((t1.toInt() - t0.toInt()) >= 1)
    }

    fun maxHeight() = vy * (vy + 1) / 2

    override fun toString() = "$vx,$vy"
}

data class Position(
    val x: Int,
    val y: Int
) {
    fun plus(v: Velocity) = Position(
        x = x + v.vx,
        y = y + v.vy
    )
}

data class Missile(
    val position: Position,
    val velocity: Velocity
) {
    fun increment() =
        Missile(
            position.plus(velocity),
            velocity.increment()
        )
}

data class Target(
    val xRange: IntRange,
    val yRange: IntRange
) {
    fun isInside(position: Position) =
        xRange.contains(position.x) && yRange.contains(position.y)

    fun isPassed(position: Position) =
        position.x > xRange.last || position.y < yRange.first
}


fun findMaxHeightForTarget(target: Target) {
    (0..(-target.yRange.first)).forEach { vy ->
        val velocity = Velocity(6, vy)
        if (velocity.reachY(target.yRange)) {
            println("$velocity\t${velocity.maxHeight()}")
        }
    }
}

fun findMatchingVelocities(target: Target):List<Velocity> {
    return (target.yRange.first..(-target.yRange.first)).flatMap { vy ->
        (0..(target.xRange.last)).flatMap { vx ->
            val velocity = Velocity(vx, vy)
            velocity.tMatchY(target.yRange).mapNotNull { t ->
                if (velocity.matchingXAt(t, target.xRange)) {
                    velocity
                }else{
                    null
                }
            }
        }
    }.distinct()
}

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
fun main() {
    val input = inputLines("17", true)
    //val target = Target(20..30, -10..-5)
    val target = Target(244..303, -91..-54)

    findMaxHeightForTarget(target)
    println("-------")
    val n = findMatchingVelocities(target).size
    println(n)
}
