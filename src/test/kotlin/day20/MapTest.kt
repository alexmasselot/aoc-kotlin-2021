package day20

import inputLines
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import utils.Matrix
import java.io.File

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class MapTest {
    private val input = inputLines("20", true)
    private val sample = TrenchMap.build(input)

    @Test
    fun neighborsCode() {
        val map = sample.second

        val got = map.neighborsCode()

        val expected = Matrix(
            listOf(
                listOf(18, 36, 8, 16, 32),
                listOf(147, 294, 68, 129, 258),
                listOf(152, 305, 34, 12, 16),
                listOf(192, 393, 275, 103, 134),
                listOf(0, 72, 152, 312, 48),
            )
        )
        assertThat(got).isEqualTo(expected)
    }

    @Test
    fun `should combine 2 (0,0,0) to 0`() {
        val got = Map.combine(listOf(0, 0, 0), 2)
        assertThat(got).isEqualTo(0)
    }

    @Test
    fun `should combine 2 (1,0,0) to 4`() {
        val got = Map.combine(listOf(1, 0, 0), 2)
        assertThat(got).isEqualTo(4)
    }

    @Test
    fun `should combine 2 (0,1,0) to 2`() {
        val got = Map.combine(listOf(0, 1, 0), 2)
        assertThat(got).isEqualTo(2)
    }

    @Test
    fun `should combine 8 (0,1,0) to 8`() {
        val got = Map.combine(listOf(0, 4, 2), 8)
        assertThat(got).isEqualTo(34)
    }

    @Nested
    inner class Flashing {
        private val filename = "src/test/kotlin/day20/input-5326.txt"
        private val inputLines = File(filename).readLines()
        private val algo = TrenchMap.build(inputLines).first

        val n = 3
        val map = Map(
            Matrix.fill(3, 3, 0)
        )

        @Test
        fun `check boundaries after one increment`() {
            val got = map.applyAlgo(algo)
            assertThat(got.pixels.values.first()).isEqualTo(List(n + 2) { 1 })
            assertThat(got.pixels.values.last()).isEqualTo(List(n + 2) { 1 })
        }

        @Test
        fun `check boundaries after two increment`() {
            val got = map.applyAlgo(algo).applyAlgo(algo)
            assertThat(got.pixels.values.first()).isEqualTo(List(n + 4) { 0 })
            assertThat(got.pixels.values.last()).isEqualTo(List(n + 4) { 0 })
        }
    }

    @Nested
    inner class Reddit5326 {
        private val filename = "src/test/kotlin/day20/input-5326.txt"
        private val inputLines = File(filename).readLines()
        private val sample5236 = TrenchMap.build(inputLines)

        @Test
        fun `algo should be reprinted the same`() {
            val (algo, map) = sample5236

            val got = algo.map { if (it == 1) "#" else "." }.joinToString("")
            assertThat(got).isEqualTo(inputLines.first())
        }

        @Test
        fun `part 1 should 5326`() {
            val (algo, map) = sample5236

            val got = map.applyAlgo(algo).applyAlgo(algo)

            assertThat(map.pixels.dimensions).isEqualTo(100 to 100)
            assertThat(got.pixels.dimensions).isEqualTo(104 to 104)
            assertThat(got.count()).isEqualTo(5326)
        }
    }

    @Nested
    inner class Sample {
        @Test
        fun `input-sample should count 35`() {
            val (algo, map) = sample

            val got = map.applyAlgo(algo).applyAlgo(algo).count()
            assertThat(got).isEqualTo(35)
        }

        @Test
        fun `input-sample should match the pattern after 1 step`() {
            val (algo, map) = sample

            val got = map.applyAlgo(algo).grow().grow().grow().grow()
            val expected = """...............
...............
...............
...............
.....##.##.....
....#..#.#.....
....##.#..#....
....####..#....
.....#..##.....
......##..#....
.......#.#.....
...............
...............
...............
..............."""
            assertThat(got.toString()).isEqualTo(expected)
        }

        @Test
        fun `input-sample should match the pattern after 2 steps`() {
            val (algo, map) = sample

            val got = map.applyAlgo(algo).applyAlgo(algo).grow().grow().grow()
            val expected = """...............
...............
...............
..........#....
....#..#.#.....
...#.#...###...
...#...##.#....
...#.....#.#...
....#.#####....
.....#.#####...
......##.##....
.......###.....
...............
...............
..............."""
            assertThat(got.toString()).isEqualTo(expected)
        }
    }
}
