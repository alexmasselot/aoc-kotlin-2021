package day21

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class PlayerPlayIntTest {

    @Test
    fun `printPosition 4 8`() {
        val got = PlayerPlayInt.toString(PlayerPlayInt.position2Int(3, 2, 7, 4, true))

        assertThat(got).isEqualTo("2 @4 x 4 @8 toGo=1")
    }

    @Test
    fun `next with player 2`() {
        val got = PlayerPlayInt.toString(PlayerPlayInt.position2Int(3, 2, 7, 4, false))

        assertThat(got).isEqualTo("2 @4 x 4 @8 toGo=2")
    }

    @Nested
    inner class Position2Int {
        @Test
        fun `1 @10 x 19 @10 toGo=1`() {
            val got = PlayerPlayInt.position2Int(9, 1, 9, 19, true)

            val expected = 0 +
                9 * 2 +
                9 * 2 * 10 +
                1 * 2 * 10 * 10 +
                19 * 2 * 10 * 10 * 21

            assertThat(got).isEqualTo(expected)
        }

        @Test
        fun `4 @3 x 19 @10 toGo=2`() {
            val got = PlayerPlayInt.position2Int(2, 4, 9, 19, false)

            val expected = 1 +
                2 * 2 +
                9 * 2 * 10 +
                4 * 2 * 10 * 10 +
                19 * 2 * 10 * 10 * 21

            assertThat(got).isEqualTo(expected)
        }
    }

    @Nested
    inner class Next {
        @Test
        fun `moving 1 should highlight 7 positions`() {
            val starter = PlayerPlayInt.position2Int(3, 2, 7, 4, true)
            val got = PlayerPlayInt.next(starter).map { PlayerPlayInt.toString(it) }.joinToString("\n")

            val expected = """
                9 @7 x 4 @8 toGo=2
                10 @8 x 4 @8 toGo=2
                11 @9 x 4 @8 toGo=2
                12 @10 x 4 @8 toGo=2
                3 @1 x 4 @8 toGo=2
                4 @2 x 4 @8 toGo=2
                5 @3 x 4 @8 toGo=2
            """.trimIndent()

            assertThat(got).isEqualTo(expected)
        }

        @Test
        fun `moving 2 should highlight 7 positions`() {
            val starter = PlayerPlayInt.position2Int(3, 2, 7, 4, false)
            val got = PlayerPlayInt.next(starter).map { PlayerPlayInt.toString(it) }.joinToString("\n")

            val expected = """
                2 @4 x 5 @1 toGo=1
                2 @4 x 6 @2 toGo=1
                2 @4 x 7 @3 toGo=1
                2 @4 x 8 @4 toGo=1
                2 @4 x 9 @5 toGo=1
                2 @4 x 10 @6 toGo=1
                2 @4 x 11 @7 toGo=1
            """.trimIndent()

            assertThat(got).isEqualTo(expected)
        }
    }

    @Test
    fun `transition moves shall points to higher indices`() {
        val got = PlayerPlayInt.transitions.mapIndexed { index, list -> index to list }
            .filter { (index, list) -> list.any { it <= index && it >= 0 } }
        assertThat(got).isEmpty()
    }

}
