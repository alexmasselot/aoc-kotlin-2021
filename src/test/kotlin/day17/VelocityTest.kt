package day17

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class VelocityTest {
    @Nested
    inner class Increment {
        @Test
        fun `vertical should not change`() {
            val given = Velocity(0, 10)
            val got = given.increment()
            assertThat(got).isEqualTo(Velocity(0, 9))
        }

        @Test
        fun `rightward should dec`() {
            val given = Velocity(5, 10)
            val got = given.increment()
            assertThat(got).isEqualTo(Velocity(4, 9))
        }

        @Test
        fun `leftward should inc`() {
            val given = Velocity(-5, 10)
            val got = given.increment()
            assertThat(got).isEqualTo(Velocity(-4, 9))
        }
    }

    @Nested
    inner class TMatch {
        @Test
        fun `tForY vy=3`() {
            val velocity = Velocity(0, 3)

            val got = listOf(6, 5, 3, 0, -4, -9, -15).map { velocity.tForY(it) }

            assertThat(got).isEqualTo(listOf(4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0))
        }

        @Test
        fun `tForY vy=0`() {
            val velocity = Velocity(0, 0)

            val got = listOf(0, -1, -3, -6, -10, -15).map { velocity.tForY(it) }

            assertThat(got).isEqualTo(listOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0))
        }

        @Test
        fun `vy 3 in -10 to -5`() {
            val got = Velocity(0, 3).tMatchY(-10..-5)
            assertThat(got).isEqualTo(listOf(9))
        }


        @Test
        fun `vy 0 in -10 to -5`() {
            val got = Velocity(0, 0).tMatchY(-10..-5)
            assertThat(got).isEqualTo(listOf(4, 5))
        }
    }

    @Test
    fun `velocity y should reach maxHeight 45`() {
        val velocity = Velocity(6, 9)

        val got = velocity.maxHeight()

        assertThat(got).isEqualTo(45)
    }

    @Nested
    inner class ReachY {
        private val velocity = Velocity(6, 3)

        @Test
        fun `-10 to -5 should be reached`() {
            val got = velocity.reachY(-10..-5)
            assertThat(got).isEqualTo(true)
        }

        @Test
        fun `-15 to -9 should be reached`() {
            val got = velocity.reachY(-15..-9)
            assertThat(got).isEqualTo(true)
        }

        @Test
        fun `-10 to -9 should be reached`() {
            val got = velocity.reachY(-10..-9)
            assertThat(got).isEqualTo(true)
        }

        @Test
        fun `-9 to -8 should be reached`() {
            val got = velocity.reachY(-9..-8)
            assertThat(got).isEqualTo(true)
        }

        @Test
        fun `-15 to -14 should be reached`() {
            val got = velocity.reachY(-15..-14)
            assertThat(got).isEqualTo(true)
        }

        @Test
        fun `-12 to -10 should be not reached`() {
            val got = velocity.reachY(-12..-10)
            assertThat(got).isEqualTo(false)
        }

        @Test
        fun `-10 to -5 should be reached with velocity 9`() {
            val got = Velocity(6, 9).reachY(-10..-5)
            assertThat(got).isEqualTo(true)
        }
    }

    @Nested
    inner class MatchingXAt {
        @Test
        fun `x for t`() {
            val velocity = Velocity(7, 2)

            val got = (0..9).map { velocity.xForT(it) }
            assertThat(got).isEqualTo(listOf(0, 7, 13, 18, 22, 25, 27, 28, 28, 28))
        }

        @Test
        fun `7,2 should reach target`() {
            val velocity = Velocity(7, 2)
            val got = velocity.matchingXAt(7, 20..30)
            assertThat(got).isEqualTo(true)
        }
    }
}
