package utils

/**
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */

enum class ShiftDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class InconsistentNumberOfColumnsException(n: Int, nCols: Int) :
    RuntimeException("Not all lines have the same number of element $n/$nCols")

class NotSAmeDimensionsException(nRows1: Int, nCols1: Int, nRows2: Int, nCols2: Int) :
    RuntimeException("Matrices do not have the same size ($nRows1, $nCols1) vs ($nRows2, $nCols2)")

data class Matrix<T>(
    val values: List<List<T>>
) {
    val nRows = values.size
    val nCols = values.first().size

    val dimensions = nRows to nCols

    init {
        values.find { it.size != nCols }?.let {
            throw InconsistentNumberOfColumnsException(it.size, nCols)
        }
    }

    fun get(row: Int, col: Int) = values[row][col]
    fun set(row: Int, col: Int, value: T): Matrix<T> {
        val rowOrig = values[row]
        return Matrix(
            values.take(row)
                .plusElement(rowOrig.take(col).plus(value).plus(rowOrig.drop(col + 1)))
                .plus(values.drop(row + 1))
        )
    }

    fun count(f: (T) -> Boolean) =
        values.map {
            it.filter(f).size
        }.sum()

    fun shift(dir: ShiftDirection, defaultValue: T) =
        when (dir) {
            ShiftDirection.UP -> Matrix(values.drop(1).plusElement(List<T>(nCols) { defaultValue }))
            ShiftDirection.DOWN -> Matrix(
                listOf(List<T>(nCols) { defaultValue }).plus(values.dropLast(1))
            )
            ShiftDirection.LEFT -> Matrix(
                values.map { listOf(defaultValue).plus(it.dropLast(1)) }
            )
            ShiftDirection.RIGHT -> Matrix(
                values.map { it.drop(1).plus(defaultValue) }
            )
        }

    fun cshift(dir: ShiftDirection) =
        when (dir) {
            ShiftDirection.UP -> Matrix(values.drop(1).plusElement(values.first()))
            ShiftDirection.DOWN -> Matrix(
                listOf(values.last()).plus(values.dropLast(1))
            )
            ShiftDirection.LEFT -> Matrix(
                values.map { listOf(it.last()).plus(it.dropLast(1)) }
            )
            ShiftDirection.RIGHT -> Matrix(
                values.map { it.drop(1).plus(it.first()) }
            )
        }

    fun <S, R> combine(other: Matrix<S>, op: (T, S) -> R): Matrix<R> {
        return zip(other).map { (a, b) -> op(a, b) }
    }

    fun <S> map(f: (T) -> S) =
        Matrix(
            values.map {
                it.map { x -> f(x) }
            }
        )

    fun <S> zip(other: Matrix<S>): Matrix<Pair<T, S>> {
        assertSameSize(other)
        return Matrix(
            values.zip(other.values).map { (row1, row2) ->
                row1.zip(row2)
            }
        )
    }

    fun zipN(vararg others: Matrix<T>): Matrix<List<T>> {
        val m0 = map { listOf(it) }
        others.forEach { assertSameSize(it) }
        return others.fold(m0) { mAcc, other -> mAcc.zip(other).map { (xs, x) -> xs.plus(x) } }
    }

    // add rows and cols on the surrounding
    fun expand(value: T): Matrix<T> =
        Matrix(
            listOf(List(nCols + 2) { value })
                .plus(values.map { listOf(value).plus(it).plus(value) })
                .plus(listOf(List(nCols + 2) { value }))
        )

    fun plusCols(other: Matrix<T>): Matrix<T> {
        assertSameSizeRows(other)
        return Matrix(
            values.zip(other.values).map { (r, ro) ->
                r.plus(ro)
            }
        )
    }

    fun plusRows(other: Matrix<T>): Matrix<T> {
        assertSameSizeCols(other)
        return Matrix(
            values.plus(other.values)
        )
    }

    private fun <S> assertSameSize(other: Matrix<S>) {
        assertSameSizeRows(other)
        assertSameSizeCols(other)
    }

    private fun <S> assertSameSizeRows(other: Matrix<S>) {
        if (other.nRows != nRows) {
            throw NotSAmeDimensionsException(nRows, nCols, other.nRows, other.nCols)
        }
    }

    private fun <S> assertSameSizeCols(other: Matrix<S>) {
        if (other.nCols != nCols) {
            throw NotSAmeDimensionsException(nRows, nCols, other.nRows, other.nCols)
        }
    }

    fun toString(sep: String = " ") =
        values.map { it.joinToString(sep) }.joinToString("\n")

    companion object {
        fun <T> create(xs: List<List<T>>) =
            Matrix(
                xs.map {
                    emptyList<T>().plus(it.toList())
                }.toList()
            )

        fun <T> fill(nRows: Int, nCols: Int, defaultValue: T) =
            Matrix((1..nRows).map {
                (1..nCols).map { defaultValue }
            })
    }
}
