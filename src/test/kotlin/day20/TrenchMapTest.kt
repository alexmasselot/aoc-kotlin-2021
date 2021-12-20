package day20

import inputLines
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class TrenchMapTest{
    @Test
    fun `should build`(){
        val input = inputLines("20", true)
        val (algo, map) = TrenchMap.build(input)

        assertThat(algo).hasSize(512)
        assertThat(map.pixels.nRows).isEqualTo(5)
        assertThat(map.pixels.nCols).isEqualTo(5)

    }
}
