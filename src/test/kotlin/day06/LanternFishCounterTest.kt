package day06

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class LanternFishCounterTest {
    @Test
    fun fromInitialAges() {
        val lfc = LanternFishCounter.fromInitialAges(7, "3,4,3,1,2")

        assertThat(lfc.cycle).isEqualTo(7)
        assertThat(lfc.distribution.toList().map{it.toInt()}).isEqualTo(listOf(0, 1, 1, 2, 1, 0, 0, 0, 0))
    }


    @ParameterizedTest
    @ValueSource(
        strings =
        [
            "3,4,3,1,2/2,3,2,0,1",
            "2,3,2,0,1/1,2,1,6,0,8",
            "0,1,0,5,6,0,1,2,2,3,0,1,2,2,2,3,3,4,4,5,7,8/6,0,6,4,5,6,0,1,1,2,6,0,1,1,1,2,2,3,3,4,6,7,8,8,8,8"
        ]
    )
    fun `increment`(input: String) {
        val inputs = input.split("/")
        val lfc = LanternFishCounter.fromInitialAges(7, inputs[0])

        val got = lfc.increment()

        val expected = LanternFishCounter.fromInitialAges(7, inputs[1])
        assertThat(got).isEqualTo(expected)
    }

    @Test
    fun incrementFor_18() {
        val given = LanternFishCounter.fromInitialAges(7, "3,4,3,1,2")

        val got = given.incrementFor(18)

        val expected = LanternFishCounter.fromInitialAges(7, "6,0,6,4,5,6,0,1,1,2,6,0,1,1,1,2,2,3,3,4,6,7,8,8,8,8")
        assertThat(got).isEqualTo(expected)

    }
    @Test
    fun incrementFor_256() {
        val given = LanternFishCounter.fromInitialAges(7, "3,4,3,1,2")

        val got = given.incrementFor(256)

        assertThat(got.count()).isEqualTo(26984457539)

    }
}
