package day14

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class PolymerMainTest {
    val step0 = "NNCB"
    val step1 = "NCNBCHB"
    val step2 = "NBCCNBBBCBHCB"
    val step3 = "NBBBCNCCNBBNBNBBCHBHHBCHB"

    val input = """CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C""".split("\n")

    @Nested
    inner class MolPairTest {
        @Test
        fun `build from sample`() {
            val got = MolPair.build(step0)

            val expected = MolPair(
                mapOf(
                    "NN" to 1,
                    "NC" to 1,
                    "CB" to 1,
                ),
                'B'
            )
            assertThat(got).isEqualTo(expected)
        }

        @Test
        fun `countChars`() {
            val molPair2 = MolPair.build(step2)
            val got = molPair2.countChars()

            assertThat(got).isEqualTo(
                mapOf(
                    'N' to 2L,
                    'B' to 6L,
                    'C' to 4L,
                    'H' to 1L,
                )
            )
        }
    }

    @Nested
    inner class PolymerTest {
        private val molPair0 = MolPair.build(step0)
        private val molPair1 = MolPair.build(step1)
        private val molPair2 = MolPair.build(step2)
        private val molPair3 = MolPair.build(step3)

        private val polymer = Polymer.read(input)

        @Test
        fun `step 0 to 1`() {
            val got = polymer.process(molPair0)

            assertThat(got).isEqualTo(molPair1)
        }

        @Test
        fun `step 1 to 2`() {
            val got = polymer.process(molPair1)

            assertThat(got).isEqualTo(molPair2)
        }

        @Test
        fun `step 2 to 3`() {
            val got = polymer.process(molPair2)

            assertThat(got).isEqualTo(molPair3)
        }


        @Test
        fun `process 10`(){
            val got =polymer.process(molPair0, 10).countChars()
            assertThat(got).isEqualTo(
                mapOf(
                    'N' to 865L,
                    'B' to 1749L,
                    'C' to 298L,
                    'H' to 161L,
                )
            )
        }

        @Test
        fun `delta chars`(){
            val got =polymer.process(molPair0, 10).deltaChar()
            assertThat(got).isEqualTo(1588)
        }
    }

}
