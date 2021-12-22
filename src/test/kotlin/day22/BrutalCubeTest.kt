package day22

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class BrutalCubeTest {
    val switches = """on x=10..12,y=10..12,z=10..12
on x=11..13,y=11..13,z=11..13
off x=9..11,y=9..11,z=9..11
on x=10..10,y=10..10,z=10..10""".split("\n")
        .map { Switch.build(it) }

    @Test
    fun firstStep() {
        val cube = BrutalCube(50)

        cube.switch(switches.first())
        assertThat(cube.countOn()).isEqualTo(27)
    }

    @Test
    fun `4 steps`() {
        val cube = BrutalCube(50)

        switches.forEach {
            cube.switch(it)
        }
        assertThat(cube.countOn()).isEqualTo(39)
    }

    @Test
    fun `switch pb 1`(){
        val cube = BrutalCube(50)
        val switch = Switch( x=64986 to 94412, y=-21979 to  382, z=-26060 to 3375, isOn=true)

        cube.switch(switch)
    }
}
