package day18

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class SFNumberTest {
    data class ExplodeAssertion(
        val given: String,
        val explode: String?,
        val expected: String
    )

    data class PlusReduceAssertion(
        val p1: String,
        val p2: String,
        val expected: String
    )

    @ParameterizedTest
    @MethodSource("explosionProvider")
    fun `find explosion target`(explodeAssertion: ExplodeAssertion) {
        val given = SFNumber.build(explodeAssertion.given)

        val got = given.findExplosionTarget()

        assertThat(got?.let { it.toString() }).isEqualTo(explodeAssertion.explode)
    }

    @ParameterizedTest
    @MethodSource("explosionProvider")
    fun `actually explode`(explodeAssertion: ExplodeAssertion) {
        val given = SFNumber.build(explodeAssertion.given)

        given.explode()

        assertThat(given.toString()).isEqualTo(explodeAssertion.expected)
    }

    @Test
    fun `findAndSplit 1`(){
        val sf = SFNumber.build("[1,[15,12]]")

        val got = sf.findAndSplit()

        assertThat(got).isEqualTo(true)
        assertThat(sf.toString()).isEqualTo("[1,[[7,8],12]]")
    }

    @Test
    fun `reduce should call explode until the end`() {
        val given = SFNumber.build("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]")

        given.reduce()

        val expected = SFNumber.build("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")

        assertThat(given).isEqualTo(expected)
    }


    @ParameterizedTest
    @MethodSource("pluseReduceProvider")
    fun `plus and reduce`(pra: PlusReduceAssertion) {
        val sf1 = SFNumber.build(pra.p1)
        val sf2 = SFNumber.build(pra.p2)

        val sf = sf1.plus(sf2)

        sf.reduce()

        assertThat(sf.toString()).isEqualTo(pra.expected)
    }



    @Nested
    inner class SFNumberCompanion {
        @Nested
        inner class SplitBracketBlock {
            @Test
            fun `should two`() {
                val got = SFNumber.splitBracketBlock("[[1,9],[8,5]]")

                assertThat(got).isEqualTo("[1,9]" to "[8,5]")
            }

            @Test
            fun `should not consume all in longer`() {
                val got = SFNumber.splitBracketBlock("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]")

                assertThat(got).isEqualTo("[[[1,2],[3,4]],[[5,6],[7,8]]]" to "9")
            }

            @Test
            fun `should not consume all in longer, inverted`() {
                val got = SFNumber.splitBracketBlock("[9,[[[1,2],[3,4]],[[5,6],[7,8]]]]")

                assertThat(got).isEqualTo("9" to "[[[1,2],[3,4]],[[5,6],[7,8]]]")
            }
        }

        @Test
        fun `build 1`() {
            val given = "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]"

            val got = SFNumber.build(given)
            val expected = SFNumberPair(
                0,
                true,
                SFNumberPair(
                    1,
                    true,
                    SFNumberPair(
                        2,
                        true,
                        SFNumberPair(
                            3,
                            true,
                            SFNumberRegular(4, true, 1),
                            SFNumberRegular(4, false, 2)
                        ),
                        SFNumberPair(
                            3,
                            false,
                            SFNumberRegular(4, true, 3),
                            SFNumberRegular(4, false, 4)
                        )
                    ),
                    SFNumberPair(
                        2,
                        false,
                        SFNumberPair(
                            3,
                            true,
                            SFNumberRegular(4, true, 5),
                            SFNumberRegular(4, false, 6)
                        ),
                        SFNumberPair(
                            3,
                            false,
                            SFNumberRegular(4, true, 7),
                            SFNumberRegular(4, false, 8)
                        )
                    )
                ),
                SFNumberRegular(1, false, 9)
            )

            assertThat(got).isEqualTo(expected)
        }

        @Test
        fun `build 2`() {
            val given = "[[[[[9,8],1],2],3],4]"

            val got = SFNumber.build(given)

            val expected = SFNumberPair(
                0,
                true,
                SFNumberPair(
                    1,
                    true,
                    SFNumberPair(
                        2,
                        true,
                        SFNumberPair(
                            3,
                            true,
                            SFNumberPair(
                                4,
                                true,
                                SFNumberRegular(5, true, 9),
                                SFNumberRegular(5, false, 8)
                            ),
                            SFNumberRegular(
                                4,
                                false,
                                1
                            )
                        ),
                        SFNumberRegular(
                            3,
                            false,
                            2
                        )
                    ),
                    SFNumberRegular(
                        2,
                        false,
                        3
                    )
                ),
                SFNumberRegular(
                    1,
                    false,
                    4
                )
            )

            assertThat(got).isEqualTo(expected)
        }

    }

    @Test
    fun `plus to add to SFNumber`() {
        val sf1 = SFNumber.build("[1,[2,3]]")
        val sf2 = SFNumber.build("[[4,5],6]")

        val got = sf1.plus(sf2)
        val expected = SFNumberPair(
            0,
            true,
            SFNumberPair(
                1,
                true,
                SFNumberRegular(2, true, 1),
                SFNumberPair(
                    2,
                    false,
                    SFNumberRegular(3, true, 2),
                    SFNumberRegular(3, false, 3)
                )
            ),
            SFNumberPair(
                1,
                false,
                SFNumberPair(
                    2,
                    true,
                    SFNumberRegular(3, true, 4),
                    SFNumberRegular(3, false, 5)
                ),
                SFNumberRegular(2, false, 6),
            )
        )

        assertThat(got).isEqualTo(expected)
    }

    @Test
    fun `reduceList 1`(){
        val input ="""[1,1]
[2,2]
[3,3]
[4,4]""".split("\n")

        val got = reduceList(input)

        val expected = "[[[[1,1],[2,2]],[3,3]],[4,4]]"

        assertThat(got.toString()).isEqualTo(expected)
    }

    @Test
    fun `reduceList 2`(){
        val input ="""[1,1]
[2,2]
[3,3]
[4,4]
[5,5]""".split("\n")

        val got = reduceList(input)

        val expected = "[[[[3,0],[5,3]],[4,4]],[5,5]]"

        assertThat(got.toString()).isEqualTo(expected)
    }

    @Test
    fun `reduceList 3`(){
        val input ="""[1,1]
[2,2]
[3,3]
[4,4]
[5,5]
[6,6]""".split("\n")

        val got = reduceList(input)

        val expected = "[[[[5,0],[7,4]],[5,5]],[6,6]]"

        assertThat(got.toString()).isEqualTo(expected)
    }

    @Test
    fun `magnitude 1`(){
        val given = SFNumber.build("[[1,2],[[3,4],5]]")
        val got = given.magnitude()

        assertThat(got).isEqualTo(143)
    }

    companion object {
        @JvmStatic
        fun explosionProvider() = Stream.of(
            ExplodeAssertion("[[[[[9,8],1],2],3],4]", "[9,8]", "[[[[0,9],2],3],4]"),
            ExplodeAssertion("[[6,[5,[7,0]]],3]", null, "[[6,[5,[7,0]]],3]"),
            ExplodeAssertion("[7,[6,[5,[4,[3,2]]]]]", "[3,2]", "[7,[6,[5,[7,0]]]]"),
            ExplodeAssertion("[[6,[5,[4,[3,2]]]],1]", "[3,2]", "[[6,[5,[7,0]]],3]"),
            ExplodeAssertion("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", "[7,3]", "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"),
            ExplodeAssertion("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", "[3,2]", "[[3,[2,[8,0]]],[9,[5,[7,0]]]]"),
            ExplodeAssertion("[[[[0,7],4],[7,[[8,4],9]]],[1,1]]", "[8,4]", "[[[[0,7],4],[15,[0,13]]],[1,1]]"),
        )

        @JvmStatic
        fun pluseReduceProvider() = Stream.of(
            PlusReduceAssertion("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]", "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]", "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]"),
            PlusReduceAssertion("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]", "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]", "[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]"),
            PlusReduceAssertion("[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]", "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]", "[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]"),
        )
    }

}

