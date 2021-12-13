package day12

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class CaveSystemTest{
    val sample1 = """start-A
start-b
A-c
A-b
b-d
A-end
b-end""".split("\n")

    val sample2="""dc-end
HN-start
start-kj
dc-start
dc-HN
LN-dc
HN-end
kj-sa
kj-HN
kj-dc""".split("\n")

    @Nested
    inner class Companion{
        @Test
        fun read(){
            val got = CaveSystem.read(sample1)

            assertThat(got.caves).hasSize(6)
        }
    }

    @Nested
    inner class CaveSystemTest{
        @Test
        fun `get start`(){
            val system = CaveSystem.read(sample1)

            val got = system.getStart()

            assertThat(got.name).isEqualTo("start")
            assertThat(got.connected.map{it.name}.sorted()).isEqualTo(listOf("A", "b"))
        }

        @Nested
        inner class IsVisitingTwiceSmall {
            fun buildPath(str:String) = Path(str.split("").map{Cave(it)})
            @Test
            fun `path Abb`(){
                val path = buildPath("Abb")
                assertThat(path.isVisitingTwiceSmall()).isEqualTo(true)
            }
            @Test
            fun `path AbbAcA`(){
                val path = buildPath("AbbAcA")
                assertThat(path.isVisitingTwiceSmall()).isEqualTo(false)
            }
        }

    }

    @Nested
    inner class FindPaths{
        @Test
        fun `sample 1`(){
            val system = CaveSystem.read(sample1)
            val got = findPaths(system)

            val gotStr = got.map { it.toString() }.sorted().joinToString("\n")
            val expected = """start,A,b,A,b,A,c,A,end
start,A,b,A,b,A,end
start,A,b,A,b,end
start,A,b,A,c,A,b,A,end
start,A,b,A,c,A,b,end
start,A,b,A,c,A,c,A,end
start,A,b,A,c,A,end
start,A,b,A,end
start,A,b,d,b,A,c,A,end
start,A,b,d,b,A,end
start,A,b,d,b,end
start,A,b,end
start,A,c,A,b,A,b,A,end
start,A,c,A,b,A,b,end
start,A,c,A,b,A,c,A,end
start,A,c,A,b,A,end
start,A,c,A,b,d,b,A,end
start,A,c,A,b,d,b,end
start,A,c,A,b,end
start,A,c,A,c,A,b,A,end
start,A,c,A,c,A,b,end
start,A,c,A,c,A,end
start,A,c,A,end
start,A,end
start,b,A,b,A,c,A,end
start,b,A,b,A,end
start,b,A,b,end
start,b,A,c,A,b,A,end
start,b,A,c,A,b,end
start,b,A,c,A,c,A,end
start,b,A,c,A,end
start,b,A,end
start,b,d,b,A,c,A,end
start,b,d,b,A,end
start,b,d,b,end
start,b,end""".split("\n").sorted().joinToString("\n")
            assertThat(gotStr).isEqualTo(expected)
        }
    }
}
