package day17

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class TargetTest{
    val target = Target(20..30, -10..-5)

    @Nested
    inner class IsInside{
        @Test
        fun `within`(){
            assertThat(target.isInside(Position(20,-10))).isEqualTo(true)
        }
        @Test
        fun `left`(){
            assertThat(target.isInside(Position(19,-7))).isEqualTo(false)
        }
        @Test
        fun `right`(){
            assertThat(target.isInside(Position(31,-7))).isEqualTo(false)
        }
        @Test
        fun `up`(){
            assertThat(target.isInside(Position(25,-4))).isEqualTo(false)
        }
        @Test
        fun `down`(){
            assertThat(target.isInside(Position(25,-11))).isEqualTo(false)
        }
    }
   @Nested
    inner class IsPassed{
        @Test
        fun `within`(){
            assertThat(target.isPassed(Position(20,-10))).isEqualTo(false)
        }
        @Test
        fun `left`(){
            assertThat(target.isPassed(Position(19,-7))).isEqualTo(false)
        }
        @Test
        fun `right`(){
            assertThat(target.isPassed(Position(31,-7))).isEqualTo(true)
        }
        @Test
        fun `up`(){
            assertThat(target.isPassed(Position(25,-4))).isEqualTo(false)
        }
        @Test
        fun `down`(){
            assertThat(target.isPassed(Position(25,-11))).isEqualTo(true)
        }
    }

}
