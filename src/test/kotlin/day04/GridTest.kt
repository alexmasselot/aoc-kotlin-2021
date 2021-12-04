package day04

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class GridTest {

    @Nested
    inner class Companion {
        @Nested
        inner class Read {
            @Test
            fun `should read a 3x3`() {
                val given = listOf(
                    " 3  5  7",
                    "11 13  17",
                    "19 23  29",
                    "  "
                )

                val got = Grid.read(3, given)

                val expected = Grid(
                    3,
                    elements = mapOf(
                        3 to GridElement(3, 0, 0),
                        5 to GridElement(5, 1, 0),
                        7 to GridElement(7, 2, 0),
                        11 to GridElement(11, 0, 1),
                        13 to GridElement(13, 1, 1),
                        17 to GridElement(17, 2, 1),
                        19 to GridElement(19, 0, 2),
                        23 to GridElement(23, 1, 2),
                        29 to GridElement(29, 2, 2),
                    )
                )

                assertThat(got).isEqualTo(expected)
            }

            @Test
            fun `should throw on duplicate`() {
                val given = listOf(
                    " 3  5  7",
                    "11  7  17",
                    "19 23  29",
                    "  "
                )

                assertThatThrownBy { Grid.read(3, given) }.isInstanceOf(UnsupportedOperationException::class.java)
            }


        }
    }

    @Nested
    inner class Play {
        private var grid = Grid.read(3, " 3 5 7\n11 13 17\n19 23  29".split("\n"))

        private fun countChecked(grid: Grid) = grid.elements.values.filter { it.checked }.size
        private fun checkedValues(grid: Grid) = grid.elements.values.filter { it.checked }

        @Test
        fun `element should should not be checked`() {
            assertThat(countChecked(grid)).isEqualTo(0)
            assertThat(checkedValues(grid)).isEmpty()
        }

        @Test
        fun `play on existing element`() {
            val got = grid.play(7)

            assertThat(countChecked(grid)).isEqualTo(0)
            assertThat(countChecked(got)).isEqualTo(1)

            assertThat(checkedValues(got)).isEqualTo(listOf(GridElement(7, 2, 0, true)))
        }

        @Test
        fun `play on not existing element`() {
            val got = grid.play(42)

            assertThat(countChecked(got)).isEqualTo(0)
        }

        @Test
        fun `play two matching element out of 4`() {
            val got = grid
                .play(7)
                .play(42)
                .play(23)

            assertThat(countChecked(got)).isEqualTo(2)

            assertThat(checkedValues(got)).isEqualTo(
                listOf(
                    GridElement(7, 2, 0, true),
                    GridElement(23, 1, 2, true),
                )
            )
        }
    }

    @Nested
    inner class IsWinner {
        private var grid = Grid.read(3, " 3 5 7\n11 13 17\n19 23 29".split("\n"))

        @Test
        fun `no check grid should have null score`() {
            val playedGrid = grid

            assertThat(playedGrid.isBingoRow()).isEqualTo(false)
            assertThat(playedGrid.isBingoColumn()).isEqualTo(false)
            assertThat(playedGrid.isWinner()).isEqualTo(false)
        }


        @Test
        fun `non aligned played have null score`() {
            val playedGrid = grid
                .play(3)
                .play(7)
                .play(13)
                .play(23)
                .play(29)

            assertThat(playedGrid.isBingoRow()).isEqualTo(false)
            assertThat(playedGrid.isBingoColumn()).isEqualTo(false)
            assertThat(playedGrid.isWinner()).isEqualTo(false)
        }


        @Test
        fun `one row `() {
            val playedGrid = grid
                .play(3)
                .play(5)
                .play(7)

            assertThat(playedGrid.isWinner()).isEqualTo(true)
            assertThat(playedGrid.score()).isEqualTo(11 + 13 + +17 + 19 + 23 + 29)
        }

        @Test
        fun `one column `() {
            val playedGrid = grid
                .play(5)
                .play(13)
                .play(23)

            assertThat(playedGrid.isWinner()).isEqualTo(true)
            assertThat(playedGrid.score()).isEqualTo(3 + +7 + 11 + 17 + 19 + 29)
        }

        @Test
        fun `all played `() {
            val playedGrid = grid
                .play(3)
                .play(5)
                .play(7)
                .play(11)
                .play(13)
                .play(17)
                .play(19)
                .play(23)
                .play(29)

            assertThat(playedGrid.isWinner()).isEqualTo(true)
            assertThat(playedGrid.score()).isEqualTo(0)
        }
    }
}
