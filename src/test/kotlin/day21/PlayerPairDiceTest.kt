package day21

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class PlayerPairDiceTest {

    @Test
    fun `play 1` () {
        val given = PlayerPairDice(
            Player(4, 0),
            Player(8, 0),
            Dice(0, 0)
        )
        val got = given.play()

        assertThat(got).isEqualTo(
            PlayerPairDice(
                Player(8, 0),
                Player(10, 10),
                Dice(3, 3)
            )
        )
    }
    @Test
    fun `play 3` () {
        val given = PlayerPairDice(
            Player(4, 14),
            Player(6, 9),
            Dice(12, 12)
        )
        val got = given.play()

        assertThat(got).isEqualTo(
            PlayerPairDice(
                Player(6, 9),
                Player(6, 20),
                Dice(15, 15)
            )
        )
    }
}
