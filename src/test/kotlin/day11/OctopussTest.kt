package day11

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class OctopussTest {


    @Nested
    inner class Step {
        val step0 ="""11111
19991
19191
19991
11111"""
        val step1="""34543
40004
50005
40004
34543"""
        val step2="""45654
51115
61116
51115
45654"""

        val sampleStep0="""5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526"""

        val sampleStep1="""6594254334
3856965822
6375667284
7252447257
7468496589
5278635756
3287952832
7993992245
5957959665
6394862637"""

        val sampleStep2="""8807476555
5089087054
8597889608
8485769600
8700908800
6600088989
6800005943
0000007456
9000000876
8700006848"""

        @Test
        fun `step 1`() {
            val input = step0.split("\n")
            val grid = Grid.read(input)

            step(grid)

            val expected = step1
            assertThat(grid.toString()).isEqualTo(expected)
        }

        @Test
        fun `step 2`() {
            val input = step1.split("\n")
            val grid = Grid.read(input)

            step(grid)

            val expected = step2
            assertThat(grid.toString()).isEqualTo(expected)
        }

        @Test
        fun `sample step 1`() {
            val input = sampleStep0.split("\n")
            val grid = Grid.read(input)

            step(grid)

            val expected = sampleStep1
            assertThat(grid.toString()).isEqualTo(expected)
        }
        @Test
        fun `sample step 2`() {
            val input = sampleStep1.split("\n")
            val grid = Grid.read(input)

            step(grid)

            val expected = sampleStep2
            assertThat(grid.toString()).isEqualTo(expected)
        }
    }

    @Nested
    inner class Companion {
        @Nested
        inner class Read {
            val input = listOf(
                "11111",
                "19991",
                "19191",
                "19991",
                "11111"
            )

            @Test
            fun `should extract correct size`() {
                val got = Grid.read(input)

                assertThat(got.octopusses).hasSize(25)
                assertThat(got.nCols).isEqualTo(5)
                assertThat(got.nRows).isEqualTo(5)
            }

            @Test
            fun `upper left should have 3 neighbors`() {
                val grid = Grid.read(input)
                val got = grid.octopusses[0].neighbors

                assertThat(got.map { it.index }.sorted()).isEqualTo(listOf(1, 5, 6))
            }

            @Test
            fun `lower right should have 3 neighbors`() {
                val grid = Grid.read(input)
                val got = grid.octopusses[24].neighbors

                assertThat(got.map { it.index }.sorted()).isEqualTo(listOf(18, 19, 23))
            }

            @Test
            fun `a middle should have 9 neighbors`() {
                val grid = Grid.read(input)
                val got = grid.octopusses[6].neighbors

                assertThat(got.map { it.index }.sorted()).isEqualTo(listOf(0, 1, 2, 5, 7, 10, 11, 12))
            }
        }
    }
}
