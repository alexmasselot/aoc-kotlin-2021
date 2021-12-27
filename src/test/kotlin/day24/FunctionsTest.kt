package day24

import day24.Functions.Companion.parse
import day24.Functions.Companion.parseDirects
import day24.Functions.Companion.parseOne
import day24.Functions.Companion.parsePuzzle
import inputLines
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.math.floor


/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class FunctionsTest {
    @Nested
    inner class Apply {

    }

    @Nested
    inner class RegFunction {
        val register = Register(2, 3, 5, 8)

        @Test
        fun `add`() {
            val got = Functions.add(register, 1, 10)
            assertThat(got).isEqualTo(Register(2, 13, 5, 8))
        }

        @Test
        fun `mul`() {
            val got = Functions.mul(register, 1, 10)
            assertThat(got).isEqualTo(Register(2, 30, 5, 8))
        }

        @Test
        fun `div`() {
            val got = Functions.div(register, 3, 3)
            assertThat(got).isEqualTo(Register(2, 3, 5, 2))
        }

        @Test
        fun `mod`() {
            val got = Functions.mod(register, 2, 3)
            assertThat(got).isEqualTo(Register(2, 3, 2, 8))
        }

        @Test
        fun `eql not`() {
            val got = Functions.eql(register, 0, 10)
            assertThat(got).isEqualTo(Register(0, 3, 5, 8))
        }

        @Test
        fun `eql true`() {
            val got = Functions.eql(register, 0, 2)
            assertThat(got).isEqualTo(Register(1, 3, 5, 8))
        }
    }

    data class ParseOneAssertion(
        val str: String,
        val w: Int,
        val x: Int,
        val y: Int,
        val z: Int,
    )

    @ParameterizedTest
    @MethodSource("parseOneProvider")
    fun parseOne(a: ParseOneAssertion) {
        val register = Register(2, 3, 5, 8)

        val f = parseOne(a.str)
        val got = f(register)

        val expected = Register(a.w, a.x, a.y, a.z)
        assertThat(got).isEqualTo(expected)
    }

    data class ParseMultiAssertion(
        val strs: List<String>,
        val w: Int,
        val x: Int,
        val y: Int,
        val z: Int,
    ) {

    }

    @ParameterizedTest
    @MethodSource("parseMultiProvider")
    fun `should parse a list of functions`(a: ParseMultiAssertion) {
        val register = Register(2, 3, 5, 8)

        val fs = parse(a.strs)
        val got = fs.apply(register)

        val expected = Register(a.w, a.x, a.y, a.z)
        assertThat(got).isEqualTo(expected)
    }

    @Test
    fun `z * 3 = x yes`() {
        val lines = """
            mul z 3
            eql z x
        """.trimIndent().split("\n")
        val fs = parse(lines)

        val register = Register(42, 9, 42, 3)

        val got = fs.apply(register)
        assertThat(got.get('z')).isEqualTo(1)
    }

    @Test
    fun `z * 3 = x no`() {
        val lines = """
            mul z 3
            eql z x
        """.trimIndent().split("\n")
        val fs = parse(lines)

        val register = Register(42, 9, 42, 2)

        val got = fs.apply(register)
        assertThat(got.get('z')).isEqualTo(0)
    }

    @Test
    fun `binary representation`() {
        val lines = """
add z w
mod z 2
div w 2
add y w
mod y 2
div w 2
add x w
mod x 2
div w 2
mod w 2
        """.trimIndent().split("\n")
        val fs = parse(lines)

        val register = Register(5, 0, 0, 0)

        val got = fs.apply(register)
        assertThat(got).isEqualTo(Register(0, 1, 0, 1))
    }

    @Nested
    inner class Puzzle {
        val fs = parsePuzzle(inputLines("24", false))
        val fDirects = parseDirects(inputLines("24", false))

        @Test
        fun `should have read the 14 functions list`() {
            assertThat(fs).hasSize(14)
        }

        @Test
        fun `should have read the 14 params`() {
            assertThat(fDirects).hasSize(14)
        }

        @Nested
        inner class PuzzleFunction() {

            @Test
            fun `just one test`() {
                val i = 2
                val fStep = fs[i]
                val fDirect = fDirects[i]

                val reg = Register(2, 3, 5, 8)

                val gotSteps = fStep.apply(reg)
                val gotDirect = fDirect(reg)

                assertThat(gotDirect).isEqualTo(gotSteps)
            }

        }


    }
    @ParameterizedTest
    @MethodSource("randomFunctionProvider")
    fun `stepping or direct function shall  given the same result for randome values`(xs: List<Int>) {
        val fs = parsePuzzle(inputLines("24", false))
        val fDirects = parseDirects(inputLines("24", false))

        val (i, w, x, y, z) = xs
        val fStep = fs[i]
        val fDirect = fDirects[i]
        val reg = Register(w, x, y, z)

        val gotSteps = fStep.apply(reg)
        val gotDirect = fDirect(reg)

        assertThat(gotDirect).isEqualTo(gotSteps)
    }
    companion object {
        @JvmStatic
        fun parseOneProvider() = Stream.of(
            ParseOneAssertion("add x w", 2, 5, 5, 8),
            ParseOneAssertion("add x 42", 2, 45, 5, 8),
            ParseOneAssertion("add x -42", 2, -39, 5, 8),
            ParseOneAssertion("mul x w", 2, 6, 5, 8),
            ParseOneAssertion("mul w x", 6, 3, 5, 8),
            ParseOneAssertion("mul x y", 2, 15, 5, 8),
            ParseOneAssertion("mul y x", 2, 3, 15, 8),
            ParseOneAssertion("div y x", 2, 3, 1, 8),
            ParseOneAssertion("mod y x", 2, 3, 2, 8),
            ParseOneAssertion("eql z x", 2, 3, 5, 0),
        )

        @JvmStatic
        fun parseMultiProvider() = Stream.of(
            ParseMultiAssertion(listOf("add x w", "mul z x"), 2, 5, 5, 40),
        )

        @JvmStatic
        fun randomFunctionProvider() = (0..100).toList().stream().map { i ->
            val i = floor(Math.random() * 14).toInt()
            val w = floor(Math.random() * 101).toInt() - 50
            val x = floor(Math.random() * 101).toInt() - 50
            val y = floor(Math.random() * 101).toInt() - 50
            val z = floor(Math.random() * 101).toInt() - 50
            listOf(i, w, x, y, z)
        }

    }
}
