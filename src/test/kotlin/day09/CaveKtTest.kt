package day09

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class CaveKtTest {

    @Test
    fun `zip n`() {
        val given = listOf(
            listOf(1, 2, 3, 4),
            listOf(10, 20, 30, 40),
            listOf(100, 200, 300, 400)
        )

        val got = zipN(given)

        assertThat(got).isEqualTo(
            listOf(
                listOf(1, 10, 100),
                listOf(2, 20, 200),
                listOf(3, 30, 300),
                listOf(4, 40, 400),
            )
        )
    }
}
