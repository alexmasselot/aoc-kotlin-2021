package utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class MatrixTest {

    @Nested
    inner class Companion {
        @Test
        fun `read 3 by 2`() {
            val input = listOf(
                listOf<Int?>(1, 2, 3),
                listOf<Int?>(10, 20, 30),
            )

            val got = Matrix.create<Int?>(
                input
            )

            assertThat(got.nRows).isEqualTo(2)
            assertThat(got.nCols).isEqualTo(3)
        }

        @Test
        fun `should fill with 0`() {
            val got = Matrix.fill(7, 9, 0)

            assertThat(got.nRows).isEqualTo(7)
            assertThat(got.nCols).isEqualTo(9)
            assertThat(got.get(2, 4)).isEqualTo(0)
        }

        @Test
        fun `set should chenge the value and keep the same size`() {
            val given = Matrix.fill(7, 9, 0)
            val got = given.set(6, 8, 42)

            assertThat(got.nRows).isEqualTo(7)
            assertThat(got.nCols).isEqualTo(9)
            assertThat(got.get(2, 4)).isEqualTo(0)
            assertThat(got.get(6, 8)).isEqualTo(42)
        }
    }

    @Test
    fun `should expand with -1`() {
        val matrix = Matrix.create(
            listOf(
                listOf(1, 2, 3),
                listOf(10, 20, 30),
                listOf(100, 200, 300),
            )
        )
        val got = matrix.expand(-1)

        val expected = Matrix.create(
            listOf(
                listOf(-1, -1, -1, -1, -1),
                listOf(-1, 1, 2, 3, -1),
                listOf(-1, 10, 20, 30, -1),
                listOf(-1, 100, 200, 300, -1),
                listOf(-1, -1, -1, -1, -1),
            )
        )
        assertThat(got).isEqualTo(expected)
    }

    @Test
    fun `map should transform`() {
        val matrix = Matrix.create(
            listOf(
                listOf(1, 2, 3),
                listOf(10, 20, 30),
                listOf(100, 200, 300),
            )
        )
        val got = matrix.map { it * 10 }

        val expected = Matrix.create(
            listOf(
                listOf(10, 20, 30),
                listOf(100, 200, 300),
                listOf(1000, 2000, 3000),
            )
        )
        assertThat(got).isEqualTo(expected)
    }

    @Test
    fun `map should zip`() {
        val matrix = Matrix.create(
            listOf(
                listOf(1, 2, 3),
                listOf(10, 20, 30),
                listOf(100, 200, 300),
            )
        )
        val matrix2 = matrix.map { it * 2 }

        val got = matrix.zip(matrix2)

        val expected = Matrix.create(
            listOf(
                listOf(1 to 2, 2 to 4, 3 to 6),
                listOf(10 to 20, 20 to 40, 30 to 60),
                listOf(100 to 200, 200 to 400, 300 to 600),
            )
        )
        assertThat(got).isEqualTo(expected)
    }


    @Test
    fun `map should zipN`() {
        val matrix = Matrix.create(
            listOf(
                listOf(1, 2, 3),
                listOf(10, 20, 30),
                listOf(100, 200, 300),
            )
        )
        val matrix2 = matrix.map { it * 2 }
        val matrix3 = matrix.map { it * 3 }

        val got = matrix.zipN(matrix2, matrix3)

        val expected = Matrix.create(
            listOf(
                listOf(listOf(1, 2, 3), listOf(2, 4, 6), listOf(3, 6, 9)),
                listOf(listOf(10, 20, 30), listOf(20, 40, 60), listOf(30, 60, 90)),
                listOf(listOf(100, 200, 300), listOf(200, 400, 600), listOf(300, 600, 900)),
            )
        )
        assertThat(got).isEqualTo(expected)
    }

    @Test
    fun `combining two matrices`() {
        val matrix = Matrix.create(
            listOf(
                listOf(1, 2, 3),
                listOf(10, 20, 30),
                listOf(100, 200, 300),
            )
        )
        val matrix2 = Matrix.create(
            listOf(
                listOf(true, false, true),
                listOf(false, true, false),
                listOf(true, false, true),
            )
        )

        val got = matrix.combine(matrix2) { a, b -> (if (b) -a else a).toString() }

        val expected = Matrix.create(
            listOf(
                listOf("-1", "2","-3"),
                listOf("10","-20","30"),
                listOf("-100","200","-300"),
            )
        )
        assertThat(got).isEqualTo(expected)
    }


    @Nested
    inner class Shift {
        val matrix = Matrix.create(
            listOf(
                listOf<Int?>(1, 2, 3),
                listOf<Int?>(10, 20, 30),
                listOf<Int?>(100, 200, 300),
            )
        )

        @Test
        fun `shift up`() {
            val got = matrix.shift(ShiftDirection.UP, null)

            val expected =
                Matrix.create(
                    listOf(
                        listOf<Int?>(10, 20, 30),
                        listOf<Int?>(100, 200, 300),
                        listOf<Int?>(null, null, null),
                    )
                )
            assertThat(got).isEqualTo(expected)
        }

        @Test
        fun `shift down`() {
            val got = matrix.shift(ShiftDirection.DOWN, null)

            val expected =
                Matrix.create(
                    listOf(
                        listOf<Int?>(null, null, null),
                        listOf<Int?>(1, 2, 3),
                        listOf<Int?>(10, 20, 30),
                    )
                )
            assertThat(got).isEqualTo(expected)
        }


        @Test
        fun `shift right`() {
            val got = matrix.shift(ShiftDirection.RIGHT, null)

            val expected =
                Matrix.create(
                    listOf(
                        listOf<Int?>(2, 3, null),
                        listOf<Int?>(20, 30, null),
                        listOf<Int?>(200, 300, null),
                    )
                )
            assertThat(got).isEqualTo(expected)
        }

        @Test
        fun `shift left`() {
            val got = matrix.shift(ShiftDirection.LEFT, null)

            val expected =
                Matrix.create(
                    listOf(
                        listOf<Int?>(null, 1, 2),
                        listOf<Int?>(null, 10, 20),
                        listOf<Int?>(null, 100, 200),
                    )
                )
            assertThat(got).isEqualTo(expected)
        }

        @Test
        fun `circular shift up`() {
            val got = matrix.cshift(ShiftDirection.UP)

            val expected =
                Matrix.create(
                    listOf(
                        listOf<Int?>(10, 20, 30),
                        listOf<Int?>(100, 200, 300),
                        listOf<Int?>(1, 2, 3),
                    )
                )
            assertThat(got).isEqualTo(expected)
        }

        @Test
        fun `circular shift down`() {
            val got = matrix.cshift(ShiftDirection.DOWN)

            val expected =
                Matrix.create(
                    listOf(
                        listOf<Int?>(100, 200, 300),
                        listOf<Int?>(1, 2, 3),
                        listOf<Int?>(10, 20, 30),
                    )
                )
            assertThat(got).isEqualTo(expected)
        }


        @Test
        fun `circular shift right`() {
            val got = matrix.cshift(ShiftDirection.RIGHT)

            val expected =
                Matrix.create(
                    listOf(
                        listOf<Int?>(2, 3, 1),
                        listOf<Int?>(20, 30, 10),
                        listOf<Int?>(200, 300, 100),
                    )
                )
            assertThat(got).isEqualTo(expected)
        }

        @Test
        fun `circular shift left`() {
            val got = matrix.cshift(ShiftDirection.LEFT)

            val expected =
                Matrix.create(
                    listOf(
                        listOf<Int?>(3, 1, 2),
                        listOf<Int?>(30, 10, 20),
                        listOf<Int?>(300, 100, 200),
                    )
                )
            assertThat(got).isEqualTo(expected)
        }
    }

}
