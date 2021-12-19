package day19

import inputLines
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class ScannerTest {
    private val oneScannerTxt = """--- scanner 0 ---
-1,-1,1
-2,-2,2
-3,-3,3
-2,-3,1
5,6,-4
8,0,7"""

    private val scanner5times = """--- scanner 0 ---
-1,-1,1
-2,-2,2
-3,-3,3
-2,-3,1
5,6,-4
8,0,7

--- scanner 0 ---
1,-1,1
2,-2,2
3,-3,3
2,-1,3
-5,4,-6
-8,-7,0

--- scanner 0 ---
-1,-1,-1
-2,-2,-2
-3,-3,-3
-1,-3,-2
4,6,5
-7,0,8

--- scanner 0 ---
1,1,-1
2,2,-2
3,3,-3
1,3,-2
-4,-6,5
7,0,8

--- scanner 0 ---
1,1,1
2,2,2
3,3,3
3,1,2
-6,-4,-5
0,7,-8""".split("\n")


    @Test
    fun `read one scanner`() {
        val got = Scanner.buildScanner(oneScannerTxt)

        assertThat(got.id).isEqualTo(0)
        assertThat(got.beacons.beacons.size).isEqualTo(6)
    }

    @Test
    fun `read  scanner list`() {
        val got = Scanner.buildScanners(scanner5times)

        assertThat(got.size).isEqualTo(5)
    }

    @Nested
    inner class FindMatchingRotated{
        val scanners = Scanner.buildScanners(inputLines("19", true))

        @Test
        fun `should have read 5 scanners`(){
            assertThat(scanners).hasSize(5)
        }

        @Test
        fun `align 2`(){
            val scanner1 = scanners.first()
            val scanner2 = scanners[1]

            val got = scanner1.findMatchingRotated(scanner2, 12)
            assertThat(got).isNotNull
            assertThat(got?.rotationTarget).isEqualTo(Rotation(Permutation.X_MINUS, Permutation.Y_PLUS, Permutation.Z_MINUS))
            assertThat(got?.translation).isEqualTo(Translation(-68,1246,43))
        }

        @Test
        fun `findExplanationPath should find the path`(){
            val transfos = findExplanationPath(scanners, 12)
        }
    }


}
