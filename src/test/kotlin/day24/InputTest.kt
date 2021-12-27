package day24

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class InputTest {

    @Test
    fun `create should get mask OK`() {
        val input = Input(754)

        assertThat(input.mask).isEqualTo(100L)
    }

    @Test
    fun pop() {
        val input = Input(754)
        val (next, rem) = input.pop()

        assertThat(next).isEqualTo(7)
        assertThat(rem).isEqualTo(Input(54, 10))
    }

    @Test
    fun `dec 9754 should produce 9753`() {
        val input = Input(9754)
        val got = input.dec()

        assertThat(got).isEqualTo(Input(9753, 1000))
    }

    @Test
    fun `dec 9751 should produce 9749`() {
        val input = Input(9751)
        val got = input.dec()

        assertThat(got).isEqualTo(Input(9749, 1000))
    }


    @Test
    fun `dec 9711 should produce 9699`() {
        val input = Input(9711)
        val got = input.dec()

        assertThat(got).isEqualTo(Input(9699, 1000))
    }


    @Test
    fun `dec 9151 should produce 9149`() {
        val input = Input(9151)
        val got = input.dec()

        assertThat(got).isEqualTo(Input(9149, 1000))
    }

    @Test
    fun `dec 1151 should produce 1149`() {
        val input = Input(1151)
        val got = input.dec()

        assertThat(got).isEqualTo(Input(1149, 1000))
    }

    @Test
    fun `dec 1211 should produce 1199`() {
        val input = Input(1211)
        val got = input.dec()

        assertThat(got).isEqualTo(Input(1199, 1000))
    }

    @Test
    fun `dec 1111 should throw`() {
        val input = Input(1111)

        assertThatThrownBy { input.dec() }
    }
}
