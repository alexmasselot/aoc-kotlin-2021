package day04

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */

data class GridElement(val value: Int, val x: Int, val y: Int, val checked: Boolean = false)
data class Grid(
    val size: Int,
    val elements: Map<Int, GridElement>
) {

    fun play(value: Int): Grid {
        val el = elements[value]

        return if (el == null) {
            this
        } else {
            this.copy(elements = elements.plus(value to el.copy(checked = true)))
        }
    }


    fun isBingoRow() =
        elements.values.filter { it.checked }.groupBy { it.y }.any {
            it.value.size == size
        }

    fun isBingoColumn() =
        elements.values.filter { it.checked }.groupBy { it.x }.any { it.value.size == size }

    /**
     * return the sum of unchecked element. The grid must have been checked for being a winner
     */
    fun score() = elements.values.filterNot { it.checked }.map { it.value }.sum()

    fun isWinner() = isBingoRow() || isBingoColumn()

    companion object {
        fun read(size: Int, lines: List<String>): Grid {
            val elements = lines
                .map { it.trim() }               // remove leading/training spaces spaces
                .filterNot { it == "" }          // remove empty lines
                .flatMapIndexed { y, line ->     // each line is transformed into a list
                    line.split("[ ]+".toRegex()) // break by one or more space
                        .map { it.trim() }
                        .mapIndexed { x, nStr ->
                            GridElement(value = nStr.toInt(), x = x, y = y)
                        }
                }
            if (elements.map { it.value }.distinct().size != elements.size) {
                throw UnsupportedOperationException("Duplicate value in grid")
            }
            return Grid(
                size,
                elements
                    .map { it.value to it }
                    .toMap()
            )
        }

    }
}
