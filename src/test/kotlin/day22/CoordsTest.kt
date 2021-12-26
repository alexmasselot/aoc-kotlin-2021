package day22

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import splitIgnoreEmpty

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
fun CharSequence.toCoords(): Coords {
    val t = this.trim().split("..")
    return Coords(t[0].toInt(), t[1].toInt())
}

internal class CoordsTest {

    data class OverlapAssertion(
        val s0: String,
        val s1: String,
        val expected: Boolean
    ) {
        val c0 = s0.toCoords()
        val c1 = s1.toCoords()
    }
    @ParameterizedTest
    @MethodSource("overlapProvider")
    fun `isOverlapping`(oa: OverlapAssertion) {
        val c1 = oa.c0
        val c2 = oa.c1

        val got1 = c1.isOverlapping(c2)
        val got2 = c2.isOverlapping(c1)

        assertThat(got1).isEqualTo(oa.expected)
        assertThat(got2).isEqualTo(oa.expected)
    }

    data class MinusAssertion(
        val s0: String,
        val s1: String,
        val sExpected: String
    ) {
        val c0 = s0.toCoords()
        val c1 = s1.toCoords()
        val expected = sExpected.splitIgnoreEmpty(",").map { it.toCoords() }
    }
    @ParameterizedTest
    @MethodSource("minusProvider")
    fun `minus`(ma: MinusAssertion) {
        val got = ma.c0.minus(ma.c1)

        assertThat(got).isEqualTo(ma.expected)
    }

    @Test
    fun `add a smaller`(){
        val got = "10..20".toCoords().plus("12..15".toCoords())

        assertThat(got).isEqualTo("10..20".toCoords())
    }

    @Test
    fun `add an left extensions`(){
        val got = "10..20".toCoords().plus("5..15".toCoords())

        assertThat(got).isEqualTo("5..20".toCoords())
    }

    @Test
    fun `add right extendsion`(){
        val got = "10..20".toCoords().plus("12..25".toCoords())

        assertThat(got).isEqualTo("10..25".toCoords())
    }


    @Test
    fun `add an non overlapping should fail`(){
        assertThatThrownBy {
            "10..20".toCoords().plus("22..25".toCoords())
        }
    }

    companion object {
        @JvmStatic
        fun overlapProvider() = Stream.of(
            OverlapAssertion("10..20", "1..11", true),
            OverlapAssertion("10..20", "1..10", true),
            OverlapAssertion("10..20", "15..30", true),
            OverlapAssertion("10..20", "20..30", true),
            OverlapAssertion("10..20", "12..18", true),
            OverlapAssertion("10..20", "5..30", true),
            OverlapAssertion("10..20", "5..9", false),
            OverlapAssertion("10..20", "21..29", false),
        )

        @JvmStatic
        fun minusProvider() = Stream.of(
            MinusAssertion("10..20", "1..11", "12..20"),
            MinusAssertion("10..20", "1..10", "11..20"),
            MinusAssertion("10..20", "15..30", "10..14"),
            MinusAssertion("10..20", "20..30", "10..19"),
            MinusAssertion("10..20", "12..18", "10..11,19..20"),
            MinusAssertion("10..20", "5..30", ""),
            MinusAssertion("10..20", "5..9", "10..20"),
            MinusAssertion("10..20", "21..29", "10..20"),
        )
    }
}
