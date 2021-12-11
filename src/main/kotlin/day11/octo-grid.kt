package day11

import inputLines

class Octopuss(
    val index: Int,
    var level: Int,
) {
    var neighbors = mutableSetOf<Octopuss>()

    fun increment() {
        if (level <= 9) {
            level += 1
        }
    }

    fun reset() {
        level = 0
    }

    fun isFlashing() = level > 9

    fun addNeighbor(other: Octopuss) {
        neighbors.add(other)
    }

    @Override
    override fun equals(o: Any?): Boolean =
        if (o is Octopuss) {
            o.index == index
        } else {
            false
        }

    override fun toString() = "$index: $level"
}

class Grid(
    val octopusses: List<Octopuss>,
    val nRows: Int,
    val nCols: Int
) {
    val size = octopusses.size

    fun countHaveFlashed() = octopusses.filter { it.level == 0 }.size

    override fun toString() =
        octopusses.map { it.level }
            .chunked(nCols)
            .map { it.joinToString("") }
            .joinToString("\n")


    companion object {
        fun read(input: List<String>): Grid {
            val nRows = input.filter { it.trim() != "" }.size
            val nCols = input.first().trim().length
            val octoRows = input
                .filter { it.trim() != "" }
                .mapIndexed { iRow, row ->
                    row.trim().split("")
                        .filter { it != "" }
                        .mapIndexed { iCol, x ->
                            Octopuss(index = iRow * nCols + iCol, level = x.toInt())
                        }
                }

            fun addNeighbors(ps: List<Pair<Octopuss, Octopuss>>) {
                ps.forEach { (o1, o2) ->
                    o1.addNeighbor(o2)
                    o2.addNeighbor(o1)
                }
            }
            octoRows.zipWithNext().map { (row1, row2) ->
                // n-s
                addNeighbors(row1.zip(row2))
                // ne-sw
                addNeighbors(row1.drop(1).zip(row2.dropLast(1)))
                // nw-se
                addNeighbors(row1.dropLast(1).zip(row2.drop(1)))
            }
            //e-w
            octoRows.forEach { row ->
                addNeighbors(row.zipWithNext())
            }

            return Grid(
                octopusses = octoRows.flatten(),
                nRows,
                nCols
            )
        }
    }
}

fun step(grid: Grid) {
    grid.octopusses.forEach { it.increment() }

    // the list we pass are all flashing
    fun handlerPropagate(acc: List<Octopuss>) {
        val flashingNeighbors = acc.flatMap { it.neighbors }
            .filter { !it.isFlashing() }
        flashingNeighbors.forEach { it.increment() }
        val newFlashing = flashingNeighbors.distinct()
            .filter { it.isFlashing() }
        if (newFlashing.isEmpty()) {
            return
        }
        handlerPropagate(newFlashing)
    }

    handlerPropagate(grid.octopusses.filter { it.isFlashing() })

    grid.octopusses.filter { it.isFlashing() }.forEach { it.reset() }
}

fun findFirstFullFlash(grid: Grid): Int {
    fun handler(step: Int): Int {
        if (grid.countHaveFlashed() == grid.size) {
            return step
        }
        step(grid)
        return handler(step + 1)
    }

    return handler(0)
}

fun part1(input:List<String>){
    val grid = Grid.read(input)

    val n = (1..100).map { i ->
        step(grid)
        grid.countHaveFlashed()
    }.sum()
    println(n)
}

fun part2(input:List<String>){
    val grid = Grid.read(input)
    val n = findFirstFullFlash(grid)

    println(n)

}

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
fun main() {
    val input = inputLines("11", false)

    part1(input)
    part2(input)

}
