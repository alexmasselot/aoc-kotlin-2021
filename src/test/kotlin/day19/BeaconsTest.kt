package day19

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class BeaconsTest {

    @Nested
    inner class OverlapDelta {
        val beacons1 = Beacons(
            listOf(
                Beacon(0, Position(0, 0, 0)),
                Beacon(2, Position(10, 0, 0)),
                Beacon(3, Position(0, 20, 0)),
                Beacon(4, Position(0, 0, 30)),
                Beacon(5, Position(0, 0, 9)),
            )
        )
        val beacons2 = Beacons(
            listOf(
                Beacon(0, Position(100, 200, 300)),
                Beacon(2, Position(110, 200, 300)),
                Beacon(3, Position(100, 220, 300)),
                Beacon(4, Position(100, 200, 330)),
                Beacon(5, Position(0, 0, 9)),
            )
        )

        @Test
        fun `should find 4 overlap`() {
            val got = beacons1.overlapDelta(beacons2, 4)
            assertThat(got).isEqualTo(
                TranslationMatch(
                    Translation(100, 200, 300),
                    listOf(
                        Beacon(0, Position(0, 0, 0)) to Beacon(0, Position(100, 200, 300)),
                        Beacon(2, Position(10, 0, 0)) to Beacon(2, Position(110, 200, 300)),
                        Beacon(3, Position(0, 20, 0)) to Beacon(3, Position(100, 220, 300)),
                        Beacon(4, Position(0, 0, 30)) to Beacon(4, Position(100, 200, 330)),
                    )
                )
            )
        }

        @Test
        fun `should not find 4 overlap`() {
            val got = beacons1.overlapDelta(beacons2, 5)
            assertThat(got).isNull()
        }
    }
}
