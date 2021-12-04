package day04

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class GridCollectionTest{

    @Nested
    inner class Companion{
        @Test
        fun readGrids() {
            val given = "3  5\n 7  11\n\n2  4\n8 16".split("\n")


            val got = GridCollection.read(2, given)

            val expectedGrids = listOf(
                Grid(
                    2,
                    elements = mapOf(
                        3 to GridElement(3, 0, 0),
                        5 to GridElement(5, 1, 0),
                        7 to GridElement(7, 0, 1),
                        11 to GridElement(11, 1, 1),
                    )
                ),
                Grid(
                    2,
                    elements = mapOf(
                        2 to GridElement(2, 0, 0),
                        4 to GridElement(4, 1, 0),
                        8 to GridElement(8, 0, 1),
                        16 to GridElement(16, 1, 1),
                    )
                )
            )

            assertThat(got).isEqualTo(GridCollection(2, expectedGrids))
        }
    }
    @Nested
    inner class Play{
        val lines = "5 16\n 8  11\n\n2  4\n8 16".split("\n")
        val grids = GridCollection.read(2, lines)

        @Test
        fun `straight show have not winner`(){
            assertThat(grids.winner()).isEqualTo(null);
        }

        @Test
        fun `played winner`(){
            val got = grids
                .play(8)
                .play(16)
                .winner()

            assertThat(got).isNotNull;
            assertThat(got?.score()).isEqualTo(6)
        }
    }
}
