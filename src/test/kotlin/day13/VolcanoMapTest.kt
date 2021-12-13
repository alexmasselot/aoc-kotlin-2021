package day13

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class VolcanoMapTest {
    val sample = """6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5""".split("\n")

    @Nested
    inner class MapTest {

        @Test
        fun `should read map`() {
            val map = Map.read(sample)

            assertThat(map.nRows).isEqualTo(15)
            assertThat(map.nCols).isEqualTo(11)

            val expected ="""...#..#..#.
....#......
...........
#..........
...#....#.#
...........
...........
...........
...........
...........
.#....#.##.
....#......
......#...#
#..........
#.#........"""

            assertThat(map.toString()).isEqualTo(expected)
        }
    }

    @Nested
    inner class FoldTest{
        @Test
        fun `read input`(){
            val got = Fold.readFolds(sample)

            assertThat(got).isEqualTo(listOf(
                Fold(false, 7),
                Fold(true, 5)
            ))
        }

        @Test
        fun `should fold on y`(){
            val map = Map.read(sample)
            val fold = Fold(false, 7)

            val got = fold.fold(map)
            val gotStr = got.toString()

            val expected = """#.##..#..#.
#...#......
......#...#
#...#......
.#.#..#.###
...........
..........."""
            assertThat(gotStr).isEqualTo(expected)
        }

        @Test
        fun `should fold on X`(){
            val map = Fold(false, 7).fold(Map.read(sample))
            val fold = Fold(true, 5)

            val got = fold.fold(map)
            val gotStr = got.toString()

            val expected = """#####
#...#
#...#
#...#
#####
.....
....."""
            assertThat(gotStr).isEqualTo(expected)
        }
    }
}
