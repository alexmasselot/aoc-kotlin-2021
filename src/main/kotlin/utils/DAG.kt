package utils

data class Edge<T>(val from: T, val to: T) {
    override fun toString() = "$from ---> $to"
}

/**
 *
 * This is a directed acyclic graph
 * Even though there is no control on actual cycles at costruction
 *
@author Alexandre Masselot
@Copyright L'Occitane 2021
 */
data class DAG<T>(
    val nodes: Set<T> = emptySet(),
    val edges: Set<Edge<T>> = emptySet()
) {
    private val descendantNodes: Map<T, List<T>> by lazy {
        edges.groupBy { it.from }
            .map { (n, es) -> n to es.map { it.to } }
            .toMap()
    }
    private val parentNodes: Map<T, List<T>> by lazy {
        edges.groupBy { it.to }
            .map { (n, es) -> n to es.map { it.from } }
            .toMap()
    }

    fun countNodes() = nodes.size
    fun countEdges() = edges.size

    fun contains(node: T) = nodes.contains(node)

    /**
     * add an orphan node
     */
    fun plus(node: T) = DAG(nodes.plus(node), edges)

    /**
     * add a node with a parent
     */
    fun plus(node: T, from: T) = DAG(nodes.plus(node), edges.plus(Edge(from, node)))

    fun plus(es: List<Pair<T, T>>) = DAG(
        nodes.plus(es.map { it.first }).plus(es.map { it.second }),
        edges.plus(es.map { Edge(it.first, it.second) })
    )
//
//    fun plus(es: List<Edge<T>>) = DAG(
//        nodes.plus(es.map { it.from }).plus(es.map { it.to }),
//        edges.plus(es)
//    )

    fun minus(node: T) = DAG(nodes.minus(node), edges.filter { it.to != node && it.from != node }.toSet())
    fun minus(es: List<Edge<T>>) = DAG(nodes, edges.minus(es).toSet())

    /**
     * add a liste of children from a single parent
     */
    fun plus(nodes: List<T>, from: T) = nodes.fold(this) { acc, n -> acc.plus(n, from) }

    fun childs(node: T) = descendantNodes[node] ?: emptyList()

    fun childEdges(node: T) = edges.filter { it.from == node }

    fun parents(node: T) = parentNodes[node] ?: emptyList()

    fun leaves() = nodes.minus(edges.map { it.from })

    fun startNodes() = nodes.minus(edges.map { it.to })

    fun isStartNode(node: T) = edges.filter { it.to == node }.isEmpty()

    fun topologicalSort(): List<T> {

        val toSort = ArrayDeque<T>()
        toSort.addAll(startNodes())
        val acc = mutableListOf<T>()
        var remGraph = this

        var i=0
        do {
            val node = toSort.removeFirst()
            val childEdges = remGraph.childEdges(node)
            remGraph = remGraph.minus(childEdges)

            toSort.addAll(remGraph.startNodes().intersect(childEdges.map { it.to }))
            acc.add(node)
            i+=1
            if(i%100 == 0 ){
                println(i)
            }
        } while (toSort.isNotEmpty())

        return acc.toList()
//        tailrec fun handler(remGraph: DAG<T>, toSort: List<T>, acc: List<T>): List<T> {
//            if (toSort.isEmpty()) {
//                if (remGraph.countEdges() > 0) {
//                    throw UnsupportedOperationException("not an acyclic graph")
//                }
//                return acc
//            }
//            val node = toSort.first()
//            val childEdges = remGraph.childEdges(node)
//            val newGraph = remGraph.minus(childEdges)
//            val newStarterNodes = newGraph.startNodes().intersect(childEdges.map { it.to })
//            return handler(newGraph, toSort.drop(1).plus(newStarterNodes), acc.plus(node))
//        }
//        return handler(this, startNodes().toList(), emptyList())
    }

    fun countPaths(root: T): Map<T, Long> {
        println("sorting")
        val sortedNodes = topologicalSort()
        println("sorted")
        val nodeIndex = sortedNodes.zip(0 until countNodes()).map { it.first to it.second }.toMap()

        val counts = Array(countNodes()) { 0L }
        counts[nodeIndex[root]!!] = 1L

        println("Going through nodes")
        sortedNodes.forEachIndexed { index, node ->
            childs(node).forEach { child -> counts[nodeIndex[child]!!] += counts[index] }
        }
        return sortedNodes.zip(counts.toList()).map { it.first to it.second }.toMap()
    }

    override fun toString() = "nodes:\n  " + nodes.joinToString("\n  ") + "\nedges\n  " + edges.joinToString("\n  ")
    /*
        leaving from a (root) node, walk all the paths down and combine the values
     */
//    fun <S> countPath(from: T, projectLeaf: (T) -> S): Map<S, Long> {
//        fun handler(node, acc:Map<T, S>): S{
//            if()
//        }
//    }
}
