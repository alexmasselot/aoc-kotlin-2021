package day22

import java.lang.Integer.max
import java.lang.Integer.min

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */

data class Coords(val from: Int, val to: Int) {
    fun isOverlapping(other: Coords) = !(other.to < from || other.from > to)


    fun intersects(other: Coords) = if (isOverlapping(other))
        Coords(max(from, other.from), min(to, other.to))
    else
        throw UnsupportedOperationException("$this intersects $other")

    /**
     *    ....    ....    ......
     *  ....    ........    ......
     */
    fun minus(other: Coords): List<Coords> {
        if (!isOverlapping(other)) {
            return listOf(this)
        }
        // this is included in other
        if ((other.from <= from && other.to >= to)) {
            return emptyList()
        }
        // other is included in this
        if (other.from > from && other.to < to) {
            return listOf(Coords(from, other.from - 1), Coords(other.to + 1, to))
        }
        //
        if (other.from <= from) {
            return listOf(Coords(other.to + 1, to))
        }
        //( other.to >= other.to) {
        return listOf(Coords(from, other.from - 1))
    }

    fun plus(other: Coords): Coords =
        if (!isOverlapping(other)) {
            throw UnsupportedOperationException("$this + $other")
        } else {
            Coords(min(other.from, from), max(to, other.to))
        }

    fun size() = to - from + 1

    override fun toString() = "$from..$to"
}

data class Cube(
    val x: Coords,
    val y: Coords,
    val z: Coords
) {
    fun isOverlapping(other: Cube) =
        x.isOverlapping(other.x) && y.isOverlapping(other.y) && z.isOverlapping(other.z)

    fun volume() = x.size().toLong() * y.size().toLong() * z.size().toLong()

    fun plus(other: Cube): List<Cube> {
        if (!isOverlapping(other)) {
            return listOf(this, other)
        }
        return listOf(this).plus(other.minus(this))
    }


    fun minus(other: Cube): List<Cube> {
        if (!isOverlapping(other)) {
            return listOf(this)
        }
        return x.minus(other.x).map { Cube(it, y, z) }
            .plus(y.minus(other.y).map { Cube(x.intersects(other.x), it, z) })
            .plus(z.minus(other.z).map { Cube(x.intersects(other.x), y.intersects(other.y), it) })

    }

    override fun toString() = "$x x $y x $z"

}
