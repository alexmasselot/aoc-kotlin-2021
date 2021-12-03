package day03.one

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
class Day03_1KtTest {

    @Test
    fun add_0() {
        val init = listOf(10, 10, 10, 10, 10)
        val got = add(init, 0)

        assertThat(got).isEqualTo(listOf(9, 9, 9, 9, 9))
    }


    @Test
    fun add_16() {
        val init = listOf(10, 10, 10, 10, 10)
        val got = add(init, 16)

        assertThat(got).isEqualTo(listOf(11, 9, 9, 9, 9))
    }


    @Test
    fun add_1() {
        val init = listOf(10, 10, 10, 10, 10)
        val got = add(init, 1)

        assertThat(got).isEqualTo(listOf(9, 9, 9, 9, 11))
    }

    @Test
    fun add_4() {
        val init = listOf(10, 10, 10, 10, 10)
        val got = add(init, 4)

        assertThat(got).isEqualTo(listOf(9, 9, 11, 9, 9))
    }


    @Test
    fun add_31() {
        val init = listOf(10, 10, 10, 10, 10)
        val got = add(init, 31)

        assertThat(got).isEqualTo(listOf(11, 11, 11, 11, 11))
    }

    @Test
    fun summarizeToInt_allPositive() {
        val given = listOf(10, 10, 10, 10, 10)

        val got = summarizeToInt(given)

        assertThat(got).isEqualTo(31)
    }


    @Test
    fun summarizeToInt_allNegative() {
        val given = listOf(-10, -10, -10, -10, -10)

        val got = summarizeToInt(given)

        assertThat(got).isEqualTo(0)
    }


    @Test
    fun summarizeToInt_mix() {
        val given = listOf(10, -10, 10, -10, -10)

        val got = summarizeToInt(given)

        assertThat(got).isEqualTo(20)
    }

    @Test
    fun summarizeToInt_throw() {
        val given = listOf(10, -10, 0, -10, -10)

        assertThatThrownBy { summarizeToInt(given) }
    }


}


