package day04

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */

data class GridCollection(
    val size: Int,
    val grids: List<Grid>
) {

    fun countGrids() = grids.size

    fun play(value: Int) =
        this.copy(grids = grids.map { it.play(value) })

    fun winner() =
        grids.find { it.isWinner() }

    fun removeWinners() =
        this.copy(
            size = size,
            grids.filterNot { it.isWinner() }
        )

    companion object {
        fun read(size: Int, lines: List<String>) =
            GridCollection(
                size = size,
                grids = lines.chunked(size + 1)
                    .map { Grid.read(size, it) }
            )
    }
}
