package day25

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class PopulationTest{
    val steps =listOf(
        """
            ...>...
            .......
            ......>
            v.....>
            ......>
            .......
            ..vvv..
        """.trimIndent().split("\n"),
        """
            ..vv>..
            .......
            >......
            v.....>
            >......
            .......
            ....v..
        """.trimIndent().split("\n"),
        """
            ....v>.
            ..vv...
            .>.....
            ......>
            v>.....
            .......
            .......
            """.trimIndent().split("\n"),
        """
            ......>
            ..v.v..
            ..>v...
            >......
            ..>....
            v......
            .......
            """.trimIndent().split("\n"),
    )
    @Nested
    inner class Step{
        private val popSteps = steps.map{Population.parse(it)}
        @Test
        fun `first step, baby`(){
            val got = popSteps.first().step()
            assertThat(got).isEqualTo(popSteps[1])
        }

    }
    @Nested
    inner class Companion{
        @Test
        fun parse(){
            val got = Population.parse(steps.first())

            assertThat(got.seaCucumbers.nRows).isEqualTo(7)
            assertThat(got.seaCucumbers.nCols).isEqualTo(7)
        }
        @Test
        fun `back to String`(){
            val got = Population.parse(steps.first()).toString()

            assertThat(got).isEqualTo(steps.first().joinToString("\n"))


        }
    }

}
