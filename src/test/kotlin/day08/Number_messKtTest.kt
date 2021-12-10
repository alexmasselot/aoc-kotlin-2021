package day

import day08.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class Number_messKtTest {
    val straightPermutation = charArrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g')
    val reversePermutation = straightPermutation.reversed().toCharArray()
    val examplePermutation = charArrayOf('d', 'e', 'a', 'f', 'g', 'b', 'c')

    val exampleSignal = Signal(
        "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab".split(" "),
        "cdfeb fcadb cdfeb cdbaf".split(" ")
    )

    @Nested
    inner class GeneratePermutations {
        @Test
        fun `empty should produce empty`() {
            val given = setOf<Char>()

            val got = generatePermutations(given)

            val expected = listOf<List<Int>>()
            assertThat(got).isEqualTo(expected)
        }

        @Test
        fun `singleton should produce one`() {
            val given = setOf('b')

            val got = generatePermutations(given)

            val expected = listOf(
                charArrayOf('b')
            )
            assertThat(got.map { it.toList() }).isEqualTo(expected.map { it.toList() })
        }


        @Test
        fun `two should produce two`() {
            val given = setOf('a', 'b')

            val got = generatePermutations(given)

            val expected = listOf(
                charArrayOf('a', 'b'),
                charArrayOf('b', 'a'),
            )
            assertThat(got.map { it.toList() }).isEqualTo(expected.map { it.toList() })
        }

        @Test
        fun `3 should produce 6`() {
            val given = setOf('a', 'b', 'c')

            val got = generatePermutations(given)

            val expected = listOf(
                charArrayOf('a', 'b', 'c'),
                charArrayOf('a', 'c', 'b'),
                charArrayOf('b', 'a', 'c'),
                charArrayOf('b', 'c', 'a'),
                charArrayOf('c', 'a', 'b'),
                charArrayOf('c', 'b', 'a'),
            )
            assertThat(got.map { it.toList() }).isEqualTo(expected.map { it.toList() })
        }

        @Test
        fun `7 characters is producing 7!`() {
            val given = setOf('a', 'b', 'c', 'd', 'e', 'f', 'g')

            val got = generatePermutations(given)

            assertThat(got).hasSize(1 * 2 * 3 * 4 * 5 * 6 * 7)
        }
    }

    @Nested
    inner class MapNumber {

        @Test
        fun `0 straight`() {
            val got = mapNumber(0, straightPermutation)

            assertThat(got).isEqualTo("abcefg")
        }

        @Test
        fun `0 reversed`() {
            val got = mapNumber(0, reversePermutation)

            assertThat(got).isEqualTo("abcefg")
        }

        @Test
        fun `0 example`() {
            val got = mapNumber(0, examplePermutation)

            assertThat(got).isEqualTo("abcdeg")
        }

        @Test
        fun `1 straight`() {
            val got = mapNumber(1, straightPermutation)

            assertThat(got).isEqualTo("cf")
        }

        @Test
        fun `1 reversed`() {
            val got = mapNumber(1, reversePermutation)

            assertThat(got).isEqualTo("be")
        }

        @Test
        fun `1 example`() {
            val got = mapNumber(1, examplePermutation)

            assertThat(got).isEqualTo("ab")
        }
    }

    @Test
    fun `check that the example complies`() {
        val got = checkPossible(exampleSignal, examplePermutation)

        assertThat(got).isEqualTo(true)
    }

    @Test
    fun `find example explanation`() {
        val got = findExplanation(exampleSignal)

        assertThat(got.toList()).isEqualTo(examplePermutation.toList())
    }

    @Test
    fun `example permutation to mapping`() {
        val mapping = Mapping(examplePermutation)
        println(mapping.string2Int)

        val got = mapping.map("bcdef")

        assertThat(got).isEqualTo(5)
    }

    @Test
    fun `decypher`() {
        val mapping = Mapping(examplePermutation)

        val got = mapping.decypher(listOf("bcdef", "abcdf", "bcdef", "abcdf"))

        assertThat(got).isEqualTo(5353)
    }

}
