package day09

import inputLines

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */


fun zipN(lists: List<List<Int>>): List<List<Int>> {
    val init = List<List<Int>>(lists.first().size) { emptyList() }
    return lists.fold(init) { acc, newList ->
        acc.zip(newList).map { (sub, x) -> sub.plus(x) }
    }
}

fun main() {

    val input = inputLines("09", false)

    val grid = input.joinToString("").split("").filter { it != "" }.map { it.toInt() }
    val nRow = input.size
    val nCol = input.first().trim().length
    val mask = BooleanArray(grid.size) { true }
    val right = grid.drop(1).plus(Int.MAX_VALUE)
    val left = listOf(Int.MAX_VALUE).plus(grid.dropLast(1))
    val infRow = IntArray(nCol) { Int.MAX_VALUE }.toList()
    val up = infRow.plus(grid.dropLast(nCol))
    val down = grid.drop(nCol).plus(infRow)



    val neighbors = zipN(
        listOf(
            grid,
            right,
            left,
            up,
            down
        )
    )
    val n = neighbors.filter { xs ->
        xs.first() < xs.drop(1).minOrNull()!!
    }
        .map{it.first()+1}
        .sum()

    println(n)

}
