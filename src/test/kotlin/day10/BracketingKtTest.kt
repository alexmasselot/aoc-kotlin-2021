package day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class BracketingKtTest {

    @ParameterizedTest
    @MethodSource("corruptedLineProvider")
    fun `should be corrupted`(input: Pair<String, Char?>) {
        val (line, expected) = input

        val got = corruptedChar(line)
        assertThat(got).isEqualTo(expected)
    }


    @ParameterizedTest
    @MethodSource("completeLineProvider")
    fun `should complete line`(input: Pair<String, String>) {
        val (line, expected) = input

        val got = completeLine(line)
        assertThat(got).isEqualTo(expected)
    }


    @ParameterizedTest
    @MethodSource("scoreMissingProvider")
    fun `should score missing line`(input: Pair<String, Int>) {
        val (line, expected) = input

        val got = scoreMissing(line)
        assertThat(got).isEqualTo(expected.toLong())
    }

    companion object {
        @JvmStatic
        fun corruptedLineProvider() = Stream.of(
            "<>" to null,
            "[]" to null,
            "{}" to null,
            "<>" to null,
            "<[]>" to null,
            "<[][]([][])>" to null,
            "<[][]([" to null,
            "}" to '}',
            "<}" to '}',
        )

        @JvmStatic
        fun completeLineProvider() = Stream.of(
            "<>" to "",
            "[]" to "",
            "{}" to "",
            "<>" to "",
            "<[]>" to "",
            "<[][]([][])>" to "",
            "<[][]([" to "])>",
            "[({(<(())[]>[[{[]{<()<>>" to "}}]])})]",
            "[(()[<>])]({[<{<<[]>>(" to ")}>]})"
        )

        @JvmStatic
        fun scoreMissingProvider() = Stream.of(
            "" to 0,
            ")" to 1,
            "]" to 2,
            "}" to 3,
            ">" to 4,
            "]>" to 14,
            "}}]])})]" to 288957,
        )
    }
}
