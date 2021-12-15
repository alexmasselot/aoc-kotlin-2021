package utils

import java.lang.Integer.max

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

data class Matrix<T>(
    val values: List<List<T>>
) {
    val nRows = values.size
    val nCols = values.first().size

    fun get(row: Int, col: Int) = values[row][col]

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

    fun <S> map(f: (T) -> S) =
        Matrix(
            values.map {
                it.map { x -> f(x) }
            }
        )

    fun <S> zip(other: Matrix<S>): Matrix<Pair<T, S>> =
        Matrix(
            values.zip(other.values).map { (row1, row2) ->
                row1.zip(row2)
            }
        )

    fun zipN(vararg others: Matrix<T>): Matrix<List<T>> {
        val m0 = map { listOf(it) }
        others.find { it.nRows != nRows }?.let {
            throw ArrayIndexOutOfBoundsException(max(it.nRows - 1, nRows - 1))
        }
        others.find { it.nCols != nCols }?.let {
            throw ArrayIndexOutOfBoundsException(max(it.nCols - 1, nCols - 1))
        }
        return others.fold(m0) { mAcc, other -> mAcc.zip(other).map { (xs, x) -> xs.plus(x) } }
    }

    companion object {
        fun <T> create(xs: List<List<T>>) =
            Matrix(
                xs.map {
                    emptyList<T>().plus(it.toList())
                }.toList()
            )

        fun <T> fill(nRows: Int, nCols: Int, defaultValue: T) =
            Matrix((1 until nRows).map {
                (1 until nCols).map { defaultValue }
            })
    }
}
