package day23

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class MoveTest {
    @ParameterizedTest
    @ValueSource(
        strings = [
            "8 14 9,10,11,12,13,14",
            "14 8 13,12,11,10,9,8",
            "11 13 12,13",
            "9 0 0",
            "0 9 9",
            "1 9 0,9",
            "9 1 0,1",
            "8 0 9,0",
            "5 8 4,11,10,9,8",
            "3 14 2,11,12,13,14",
            "0 5 10,11,4,5",
            "2 3 3",
            "3 2 2",
            "3 4 2,11,4",
        ]
    )
    fun `getSteps should walk through`(given: String) {
        val xs = given.split(" ")
        val from = xs[0].toInt()
        val to = xs[1].toInt()
        val expected = xs[2].split(",").map { it.toInt() }

        val got = Move(0, from, to).steps
        assertThat(got).isEqualTo(expected)
    }

    @Nested
    inner class Cost {
        @Test
        fun ` 1 5 - 10`() {
            val move = Move(1, 4, 10)

            assertThat(move.cost).isEqualTo(40)
        }

        @Test
        fun ` 1 8 - 14`() {
            val move = Move(1, 8, 14)

            assertThat(move.cost).isEqualTo(100)
        }

        @Test
        fun ` 1 12 - 14`() {
            val move = Move(1, 12, 14)

            assertThat(move.cost).isEqualTo(30)
        }

        @Test
        fun ` 1 14 - 12`() {
            val move = Move(1, 14, 12)

            assertThat(move.cost).isEqualTo(30)
        }

        @Test
        fun ` 1 12 - 2`() {
            val move = Move(1, 12, 2)

            assertThat(move.cost).isEqualTo(40)
        }

        @Test
        fun ` 1 3 - 7`() {
            val move = Move(1, 3, 7)

            assertThat(move.cost).isEqualTo(80)
        }
        @Test
        fun ` 1 7 - 3`() {
            val move = Move(1, 7, 3)

            assertThat(move.cost).isEqualTo(80)
        }

        @Test
        fun `0 5 - 10`() {
            val move = Move(0, 4, 5)

            assertThat(move.cost).isEqualTo(1)
        }

        @Test
        fun `1 5 - 10`() {
            val move = Move(1, 4, 5)

            assertThat(move.cost).isEqualTo(10)
        }

        @Test
        fun `2 5 - 10`() {
            val move = Move(2, 4, 5)

            assertThat(move.cost).isEqualTo(100)
        }

        @Test
        fun `3 5 - 10`() {
            val move = Move(3, 4, 5)

            assertThat(move.cost).isEqualTo(1000)
        }
    }

    @Nested
    inner class MutateBoard {
        val boardTxt = """
            #############
            #...........#
            ###B#C#B#A###
              #A#D#C#D#
              #########
        """.trimIndent()
        val board = Board.fromString(boardTxt)

        @Test
        fun `should not move from 8 as there are no amphipods`() {
            val m = Move(0, 8, 12)

            val got = board.isPossible(m)

            assertThat(got).isEqualTo(false)
        }

        @Test
        fun `should not move A from 1 to 8 as we would cross B on 0`() {
            val m = Move(0, 1, 2)

            val got = board.isPossible(m)

            assertThat(got).isEqualTo(false)
        }

        @Test
        fun `should not move C from 0 to 8 as it is not a C at this position`() {
            val m = Move(10, 0, 8)

            val got = board.isPossible(m)

            assertThat(got).isEqualTo(false)
        }

        @Test
        fun `should not move A from 0 to 8 as it is not a A at this position`() {
            val m = Move(0, 0, 8)

            val got = board.isPossible(m)

            assertThat(got).isEqualTo(false)
        }

        @Test
        fun `should be able to move B from 0 to 12`() {
            val m = Move(1, 0, 12)

            val got = board.isPossible(m)

            assertThat(got).isEqualTo(true)
        }

        @Test
        fun `should be able to move A from 6 to 9`() {
            val m = Move(0, 6, 9)

            val got = board.isPossible(m)

            assertThat(got).isEqualTo(true)
        }

        @Test
        fun `should not be able to move B from 0 to 3 as it is occupied`() {
            val m = Move(1, 0, 3)

            val got = board.isPossible(m)

            assertThat(got).isEqualTo(false)
        }

        @Test
        fun `should move B`() {
            val m = Move(1, 0, 12)

            val got = board.mutated(m)

            val expected = Board.fromString(
                """
            #############
            #.......B...#
            ###.#C#B#A###
              #A#D#C#D#
              #########                
            """.trimIndent()
            )
            assertThat(got).isEqualTo(expected)
        }

        @Test
        fun `should move A`() {
            val m = Move(0, 6, 9)

            val got = board.mutated(m)

            val expected = Board.fromString(
                """
            #############
            #.A.........#
            ###B#C#B#.###
              #A#D#C#D#
              #########                
            """.trimIndent()
            )
            assertThat(got).isEqualTo(expected)
        }


        @Test
        fun `should move a few to make one pod vanish`() {

            val got = board
                .mutated(Move(0, 6, 9))
                .mutated(Move(1, 0, 10))
                .mutated(Move(0, 9, 0))

            val expected = Board.fromString(
                """
            #############
            #...B.......#
            ###.#C#B#.###
              #A#D#C#D#
              #########                
            """.trimIndent()
            )
            assertThat(got).isEqualTo(expected)
        }

    }
}
