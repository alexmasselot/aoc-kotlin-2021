package day22

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import splitIgnoreEmpty

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */


fun CharSequence.toCube(): Cube {
    val t = this.split("x")
    return Cube(
        t[0].toCoords(),
        t[1].toCoords(),
        t[2].toCoords()
    )
}

internal class CubeTest {
    data class CubeOverlapAssertion(
        val s0: String,
        val s1: String,
        val expected: Boolean
    ) {
        val c0 = s0.toCube()
        val c1 = s1.toCube()
    }

    @ParameterizedTest
    @MethodSource("overlapProvider")
    fun `isOverlapping`(oa: CubeOverlapAssertion) {
        val got1 = oa.c0.isOverlapping(oa.c1)
        val got2 = oa.c1.isOverlapping(oa.c0)

        assertThat(got1).isEqualTo(oa.expected)
        assertThat(got2).isEqualTo(oa.expected)
    }

    data class PlusAssertion(
        val title: String,
        val s0: String,
        val s1: String,
        val sExpected: String
    ) {
        val c0 = s0.toCube()
        val c1 = s1.toCube()
        val expected = sExpected.splitIgnoreEmpty(",").map { it.trim().toCube() }
    }

    @Test
    fun volume() {
        val cube = Cube(Coords(2, 5), Coords(20, 50), Coords(200, 500))

        val volume = cube.volume()

        assertThat(volume).isEqualTo(4 * 31 * 301)
    }

    @ParameterizedTest
    @MethodSource("plusProvider")
    fun `plus`(pa: PlusAssertion) {
        val addition = pa.c0.plus(pa.c1)

        val volume1 = addition.sumOf { it.volume() }
        val volume2 = pa.c1.plus(pa.c0).sumOf { it.volume() }

        assertThat(addition.toSet()).isEqualTo(pa.expected.toSet())
        assertThat(volume1).isEqualTo(volume2)
    }

    data class MinusAssertion(
        val title: String,
        val s0: String,
        val s1: String,
        val sExpected: String
    ) {
        val c0 = s0.toCube()
        val c1 = s1.toCube()
        val expected = sExpected.splitIgnoreEmpty(",").map { it.trim().toCube() }
    }

    @ParameterizedTest
    @MethodSource("minusProvider")
    fun `minus`(pa: MinusAssertion) {
        val got1 = pa.c0.minus(pa.c1)

        assertThat(got1.toSet()).isEqualTo(pa.expected.toSet())
    }

    companion object {
        @JvmStatic
        fun overlapProvider() = Stream.of(
            CubeOverlapAssertion(
                "10..20 x 100..200 x 1000..3000",
                "10..25 x 80..200 x 2000..3100",
                true,
            ),
            CubeOverlapAssertion(
                "10..20 x 100..200 x 1000..3000",
                "10..25 x 80..200 x 0..999",
                false
            ),
        )

        @JvmStatic
        fun plusProvider() = Stream.of(
            PlusAssertion(
                "not overlapping",
                "10..20 x 100..200 x 1000..3000",
                "10..25 x 80..200 x 0..999",
                "10..20 x 100..200 x 1000..3000, 10..25 x 80..200 x 0..999"
            ),
            PlusAssertion(
                "pumping out central z +",
                "10..20 x 100..200 x 1000..3000",
                "12..18 x 120..180 x 2000..4000",
                "10..20 x 100..200 x 1000..3000, 12..18 x 120..180 x 3001..4000"
            ),
            PlusAssertion(
                "pumping out central z -",
                "10..20 x 100..200 x 1000..3000",
                "12..18 x 120..180 x 0..2000",
                "10..20 x 100..200 x 1000..3000, 12..18 x 120..180 x 0..999"
            ),
            PlusAssertion(
                "pumping out central y +",
                "10..20 x 100..200 x 1000..2000",
                "12..18 x 180..300 x 1200..1800",
                "10..20 x 100..200 x 1000..2000, 12..18 x 201..300 x 1200..1800"
            ),
            PlusAssertion(
                "pumping out central y -",
                "10..20 x 100..200 x 1000..2000",
                "12..18 x 0..150 x 1200..1800",
                "10..20 x 100..200 x 1000..2000, 12..18 x 0..99 x 1200..1800"
            ),
            PlusAssertion(
                "pumping out central x +",
                "10..20 x 100..200 x 1000..3000",
                "15..25 x 120..180 x 1800..2800",
                "10..20 x 100..200 x 1000..3000, 21..25 x 120..180 x 1800..2800"
            ),
            PlusAssertion(
                "pumping out central x -",
                "10..20 x 100..200 x 1000..3000",
                "5..15 x 120..180 x 1800..2800",
                "10..20 x 100..200 x 1000..3000, 5..9 x 120..180 x 1800..2800"
            ),
        )


        @JvmStatic
        fun minusProvider() = Stream.of(
            MinusAssertion(
                "not overlapping at all",
                "1..8 x 10..80 x 100..800",
                "0..2 x 0..1 x 0..1",
                "1..8 x 10..80 x 100..800"
            ),
            MinusAssertion(
                "overlapping totally",
                "1..8 x 10..80 x 100..800",
                "0..10 x 0..100 x 0..1000",
                ""
            ),
            MinusAssertion(
                "overlapping totally y + z",
                "1..8 x 10..80 x 100..800",
                "0..5 x 0..100 x 0..1000",
                "6..8 x 10..80 x 100..800"
            ),
            MinusAssertion(
                "overlapping totally x + z",
                "1..8 x 10..80 x 100..800",
                "0..10 x 0..50 x 0..1000",
                "1..8 x 51..80 x 100..800"
            ),
            MinusAssertion(
                "overlapping totally x + y",
                "1..8 x 10..80 x 100..800",
                "0..10 x 0..100 x 0..500",
                "1..8 x 10..80 x 501..800"
            ),
            MinusAssertion(
                "inner total",
                "1..8 x 10..80 x 100..800",
                "3..5 x 30..50 x 300..500",
                "1..2 x 10..80 x 100..800, 6..8 x 10..80 x 100..800, 3..5 x 10..29 x 100..800, 3..5 x 51..80 x 100..800, 3..5 x 30..50 x 100..299, 3..5 x 30..50 x 501..800"
            ),
        )
    }
}
