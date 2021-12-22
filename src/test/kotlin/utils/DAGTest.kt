package utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

/**
 * @author Alexandre Masselot
 * @Copyright L'Occitane 2021
 */
internal class DAGTest {
    /*
                     one --------> seven
                     /\              |
                    /  \             |
                   /    \            |
                  /      \           |
                 V        V          V
                two ---> three ---> six
                 /\      /
                /  \    /
               /    \  /
              /      \/
             V       VV
           five <-- four

    */
    private val graph = DAG<String>()
        .plus("one")
        .plus(listOf("two", "three"), "one")
        .plus("four", "two")
        .plus("three", "two")
        .plus("four", "three")
        .plus("five", "four")
        .plus("five", "two")
        .plus("six", "three")
        .plus("seven", "one")
        .plus("six", "seven")

    @Test
    fun `count nodes`() {
        assertThat(graph.countNodes()).isEqualTo(7)
    }

    @Test
    fun `count edges`() {
        assertThat(graph.countEdges()).isEqualTo(10)
    }

    @Test
    fun `remove edges leaving from three`() {
        val got = graph.minus(graph.childEdges("three"))
        val expected = DAG<String>()
            .plus("one")
            .plus(listOf("two", "three"), "one")
            .plus("four", "two")
            .plus("three", "two")
            .plus("five", "four")
            .plus("five", "two")
            .plus("seven", "one")
            .plus("six", "seven")
        assertThat(got).isEqualTo(expected)
    }

    @Test
    fun `remove node three`() {
        val got = graph.minus("three")
        val expected = DAG<String>()
            .plus("one")
            .plus(listOf("two"), "one")
            .plus("four", "two")
            .plus("five", "four")
            .plus("five", "two")
            .plus("seven", "one")
            .plus("six", "seven")
        assertThat(got).isEqualTo(expected)
    }

    @Test
    fun `descendants`() {
        val got = graph.childs("two")

        assertThat(got.sorted()).isEqualTo(listOf("five", "four", "three"))
    }

    @Test
    fun `should find the descendant edges`() {
        val got = graph.childEdges("two")

        val expected = listOf(
            Edge("two", "five"),
            Edge("two", "four"),
            Edge("two", "three"),
        )

        assertThat(got.sortedBy { it.to }).isEqualTo(expected)
    }

    @Test
    fun `parents`() {
        val got = graph.parents("five")

        assertThat(got.sorted()).isEqualTo(listOf("four", "two"))
    }

    @Test
    fun leaves() {
        val got = graph.leaves()

        assertThat(got.sorted()).isEqualTo(listOf("five", "six"))
    }

    @Test
    fun startNodes() {
        val got = graph.startNodes()

        assertThat(got.sorted()).isEqualTo(listOf("one"))
    }

    @Test
    fun `node one should be a starter`() {
        val got = graph.isStartNode("one")

        assertThat(got).isEqualTo(true)
    }

    @Test
    fun `node two should not be a starter`() {
        val got = graph.isStartNode("two")

        assertThat(got).isEqualTo(false)
    }

    @Test
    fun tolpologicalSort() {
        val got = graph.topologicalSort()
        assertThat(got).isEqualTo(listOf("one", "two", "seven", "three", "four", "six", "five"))
    }

    @Test
    fun `countPath from root`() {
        val got = graph.countPaths("one")
        assertThat(got).isEqualTo(
            mapOf(
                "one" to 1L,
                "two" to 1L,
                "three" to 2L,
                "four" to 3L,
                "five" to 4L,
                "six" to 3L,
                "seven" to 1L,
            )
        )
    }
    @Test
    fun `countPath from 2`() {
        val got = graph.countPaths("two")
        assertThat(got).isEqualTo(
            mapOf(
                "one" to 0L,
                "two" to 1L,
                "three" to 1L,
                "four" to 2L,
                "five" to 3L,
                "six" to 1L,
                "seven" to 0L,
            )
        )
    }

    @Test
    fun `WTF the big matrix`(){
        val n = 400000
        val matrix =  Array(n){Array(7){0} }
        println("Suh ${matrix.size}")
        println(matrix[n/2][3])
    }
}
