package day19

import day19.Permutation.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class RotationTest {
    private val scanner5timesInput = """--- scanner 0 ---
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
    val scanner5times = Scanner.buildScanners(scanner5timesInput)

    @Test
    fun `should rotate position`() {
        val pos = Position(2, 3, 7)
        val rotation = Rotation(Z_MINUS, X_PLUS, Y_MINUS)

        val got = rotation.apply(pos)

        assertThat(got).isEqualTo(Position(-7, 2, -3))
    }


    @Test
    fun `should have generated 24 rotations`(){
        assertThat(Rotation.allRotations).hasSize(24)
    }

    @Test
    fun `should find all 5 demo rotations`() {
        val allPositionRotations = Rotation.allRotations.map {
            it.apply(scanner5times.first().beacons)
        }
        assertThat(
            scanner5times.map { it.beacons }
        ).allMatch { allPositionRotations.contains(it) }

    }

}
