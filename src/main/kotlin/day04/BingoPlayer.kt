package day04

import java.io.File

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */

fun play(grids: GridCollection, draws: List<Int>): Pair<Int, Grid>? {
    if (draws.isEmpty()) {
        return null
    }

    val draw = draws.first()
    val playedGrids = grids.play(draw)
    val winner = playedGrids.winner()
    if (winner != null) {
        return draw to winner
    }
    return play(playedGrids, draws.drop(1))
}

fun playLastWinner(grids: GridCollection, draws: List<Int>): Pair<Int, Grid>? {
    if (draws.isEmpty()) {
        return null
    }
    if(grids.countGrids() == 1){
        // bifurcate to the classic draw until winning
        return play(grids, draws)
    }

    val draw = draws.first()
    val playedGrids = grids.play(draw)

    return playLastWinner(playedGrids.removeWinners(), draws.drop(1))
}
fun main() {
    val tag = "" //""-sample"
    val fileGrids = "src/main/kotlin/day04/grids$tag.txt"
    val fileDraw = "src/main/kotlin/day04/draws$tag.txt"
    val grids = GridCollection.read(5, File(fileGrids).readLines())
    val draws = File(fileDraw).readText().trim().split(",").map { it.toInt() }

    // Variant 1
    // val winner = play(grids, draws) ?: throw UnsupportedOperationException("No winner :(")
    // Variant 2
    val winner = playLastWinner(grids, draws) ?: throw UnsupportedOperationException("No winner :(")
    val (lastDraw, grid) = winner
    println(lastDraw * grid.score())
}
