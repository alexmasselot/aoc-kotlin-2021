package day09

import inputLines

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */

data class Block(
    val height: Int,
    val index: Int,
    var left: Block? = null,
    var right: Block? = null,
    var up: Block? = null,
    var down: Block? = null,
) {
    fun neighbors() = setOfNotNull(left, right, up, down)

    fun isLocalMinimum() =
        neighbors().map { it.height }.minOrNull()?.let { it > height } ?: true

    fun flowTo() = neighbors().filter { it.height < height }

    override fun toString() =
        "$index: $height left=${left?.height ?: '-'} right=${right?.height ?: '-'} up=${up?.height ?: '-'} down=${down?.height ?: '-'}" +
            if (isLocalMinimum()) {
                " V"
            } else {
                ""
            }


    @Override
    override fun equals(o: Any?): Boolean =
        if (o is Block) {
            o.index == index
        } else {
            false
        }

    override fun hashCode() = index
}

fun buildGrid(): List<Block> {
    val input = inputLines("09", false)

    val grid = input
        .joinToString("")
        .split("")
        .filter { it != "" }
        .map { it.toInt() }
        .mapIndexed { i, h -> Block(height = h, index = i) }

    val nRow = input.size
    val nCol = input.first().trim().length

    grid.zip(listOf(null).plus(grid.dropLast(1))).forEach { (g0, g1) ->
        g0.right = g1
    }
    grid.zip(grid.drop(1).plus(null)).forEach { (g0, g1) ->
        g0.left = g1
    }
    (1..nRow).forEach {
        grid[(nCol * it) - 1].left = null
    }
    (0 until nRow).forEach {
        grid[it * nCol].right = null
    }
    val nullRow = List<Block?>(nCol) { null }
    grid.zip(nullRow.plus(grid.dropLast(nCol))).forEach { (g0, g1) ->
        g0.up = g1
    }
    grid.zip(grid.drop(nCol).plus(nullRow)).forEach { (g0, g1) ->
        g0.down = g1
    }

    return grid
}

fun printList(blocks: Set<Block>) {
    blocks.sortedBy { it.index }
        .forEach { println(it) }
}

fun bassin(anchor: Block): Set<Block> {
    fun handler(accBassin: Set<Block>): Set<Block> {
//        println("bassin")
//        printList(accBassin)
        val neighbors = accBassin.flatMap { it.neighbors() }.toSet().minus(accBassin)
//        println("neighbors")
//        printList(neighbors)
//
//        println("Flowing to ")
//        neighbors.forEach {
//            println("${it.index}: ${it.height} => ${it.flowTo().map { it.index }}")
//        }

        val flowingNeighbors = neighbors
            .filter { it.height < 9 }
            .filter {
                accBassin.containsAll(it.flowTo())
            }
//        println("flowingNeighbors")
//        printList(flowingNeighbors.toSet())
        if (flowingNeighbors.isEmpty()) {
            return accBassin
        }
        return handler(accBassin.plus(flowingNeighbors))
    }
    return handler(setOf(anchor))
}

fun main() {

    val grid = buildGrid()

    val n = grid.filter { it.isLocalMinimum() }
        .map { it.height + 1 }
        .sum()

    println("n= $n")

//    grid.filter { it.isLocalMinimum() }
//        .map { bassin(it) }
//        .forEach { b ->
//            println("----------------------------")
//            b.forEach {
//                println("${it.index}: ${it.height}")
//            }
//        }

    val m = grid.filter { it.isLocalMinimum() }
        .map { bassin(it) }
        .map { it.size }
        .sorted()
        .reversed()
        .take(3)
        .fold(1) { acc, x -> acc * x }
    println(m)
}
