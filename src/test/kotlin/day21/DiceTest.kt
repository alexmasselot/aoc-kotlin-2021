package day21

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class DiceTest {
    @Test
    fun `default play goes to 2`() {
        val dice = Dice(1)

        val got = dice.play()

        assertThat(got)
            .isEqualTo(Dice(2, 1))
    }

    @Test
    fun `100 play goes to 1`() {
        val dice = Dice(100, 5)

        val got = dice.play()

        assertThat(got)
            .isEqualTo(Dice(1, 6))
    }

    @Test
    fun `play 4 times`() {
        val dice = Dice(99)

        val (gotDice, gotPoints) = dice.play(4)

        assertThat(gotDice).isEqualTo(Dice(3, 4))
        assertThat(gotPoints).isEqualTo(100 + 1 + 2 + 3)
    }
}
