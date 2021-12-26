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
internal class BoardTest {
    data class PathAssertion(
        val from: Int,
        val to: Int,
        val steps: String
    )

    @Nested
    inner class CompanionBoard {
        @Nested
        inner class FromString {
            @Test
            fun `should read first example`() {
                val given = """
        #############
        #...........#
        ###B#C#B#D###
          #A#D#C#A#
          #########
                """.trimIndent()

                val got = Board.fromString(given)

                assertThat(got.rep.toString(4)).isEqualTo("3213201")
                assertThat(got.isOccupied.toString(2)).isEqualTo("11111111")
            }

            @ParameterizedTest
            @ValueSource(
                strings = [
                    """#############
#...........#
###B#C#B#D###
  #A#D#C#A#
  #########""",
                    """#############
#...B.......#
###B#C#.#D###
  #A#D#C#A#
  #########""",
                    """#############
#...B.......#
###B#.#C#D###
  #A#D#C#A#
  #########""",
                    """#############
#.....D.....#
###B#.#C#D###
  #A#B#C#A#
  #########""",
                    """#############
#...........#
###A#B#C#D###
  #A#B#C#D#
  #########""",
                ]
            )
            fun `fromString should roll back to toString`(given: String) {
                val board = Board.fromString(given)

                val got = board.toString()

                assertThat(got).isEqualTo(given)
            }
        }
    }

    @Nested
    inner class BuildingPath {
        @Test
        fun buildPossibleOutMoves() {
            val got = Board.outMoves

            assertThat(got).hasSize(
                //4 * 2 * (6 * 7 / 2 - 2) // hallway moves
                +4 * 3 * 2 * 7   // from not my alley to anywhere
                    //  + 4 * 13  // from anywhere to my alley
                    + 4 * 7 // unblocking my alley to go in the hallway
            )
        }

        @Test
        fun `should not exist in the path`() {

            val given = Move(1, 4, 1)

            assertThat(Board.outMoves).doesNotContain(given)
        }

        @Test
        fun `should exist in the path`() {

            val given = Move(1, 4, 10)

            assertThat(Board.outMoves).contains(given)
        }


        @Test
        fun `should not create As`() {
            val board = Board.fromString(
                """
                #############
#B..........#
###.#C#B#D###
  #.#D#.#A#
  #########
            """.trimIndent()
            )

            val got = board.outMoves().filter { it.type == 0 }

            assertThat(got).hasSize(0)
        }

        @Test
        fun ` moving A 0 to 9 should not be possible`() {
            val board = Board.fromString(
                """
                #############
#B..........#
###.#C#B#D###
  #.#D#.#A#
  #########
            """.trimIndent()
            )

            val move =Move(0, 0, 9)

            println(board.isOccupied.toString(2))
            println(move.bitMove.toString(2))
            assertThat(board.isPossible(move)).isEqualTo(false)
        }
    }

    @Test
    fun `should clean up`() {
        val given = """
        #############
        #...........#
        ###B#C#B#D###
          #A#A#C#D#
          #########
                """.trimIndent()

        val got = Board.fromString(given).cleanUp()

        val expected = Board.fromString("""
        #############
        #...........#
        ###B#C#B#.###
          #.#A#.#.#
          #########
                """.trimIndent())

        assertThat(got).isEqualTo(expected to 10)
    }

    @Test
    fun `be recognized as done`() {
        val given = """
        #############
        #...........#
        ###.#.#.#.###
          #.#.#.#.#
          #########
                """.trimIndent()

        val got = Board.fromString(given).isDone



        assertThat(got).isEqualTo(true)
    }
}
