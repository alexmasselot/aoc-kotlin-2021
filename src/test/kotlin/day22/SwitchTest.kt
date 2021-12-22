package day22

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class SwitchTest {
    @Test
    fun `should build one`() {
        val got = Switch.build("on x=-20..26,y=-36..17,z=-47..7")

        assertThat(got).isEqualTo(
            Switch(
                x = -20 to 26, y = -36 to 17, z = -47 to 7, true
            )
        )
    }
}
